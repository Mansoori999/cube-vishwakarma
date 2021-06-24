package com.vinners.cube_vishwakarma.ui.outlets


import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.cache.DatabaseManager
import com.vinners.cube_vishwakarma.cache.OutletsLocalDatabase
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityOutletsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.inject.Inject
import kotlin.collections.ArrayList


class OutletsActivity : BaseActivity<ActivityOutletsBinding,OutletsViewModel>(R.layout.activity_outlets),OutletsClickListener {


    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    val db = DatabaseManager(this)
//    DatabaseHandler db = new DatabaseHandler(this);

    private lateinit var mDb: OutletsLocalDatabase
    private val mRandom: Random = Random()
    @Inject
    lateinit var userSessionManager: UserSessionManager

    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    private lateinit var bottomSheetDialog:BottomSheetDialog

    private lateinit var regionalspinner: TextView
    private lateinit var salesspinner: TextView
    private lateinit var applyBtn:TextView

    var roselectedId:String? = null
    var saleselectedId:String? = null
    var selectedItemList:String? = ""
    var saleselectedItemList:String? = ""


    var filterOutletList: ArrayList<OutletsList> = ArrayList<OutletsList>()

    private val filtersLoadedFirstTime = false

    override val viewModel: OutletsViewModel by viewModels { viewModelFactory }

    private val outletRecyclerAdapter: OutletRecyclerAdapter by lazy {
        OutletRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                    setOutletListener(this@OutletsActivity)
                }
    }



    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {

        setSupportActionBar(viewBinding.outletToolbar)
        getSupportActionBar()!!.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_left,null))
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        viewBinding.outletToolbar.setNavigationOnClickListener { view -> onBackPressed() }

        viewBinding.outletRecycler.layoutManager = LinearLayoutManager(this)
        outletRecyclerAdapter.updateViewList(emptyList())
        viewBinding.outletRecycler.adapter = outletRecyclerAdapter
//        viewBinding.refreshLayout.setOnRefreshListener {
//
//            if (!viewBinding.refreshLayout.isRefreshing) {
//                viewBinding.refreshLayout.isRefreshing = true
//            }
//            viewModel.getOutletData()
//
//        }
        val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, p1: Int) {}
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_filter_outlet_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        regionalspinner = bottomSheetView.findViewById(R.id.ro_spinner)
        salesspinner = bottomSheetView.findViewById(R.id.sales_spinner)
        sheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior.setBottomSheetCallback(bottomSheetCallback)
        applyBtn = bottomSheetView.findViewById(R.id.apply_textview)
        initBottomsheetFileterView()
//        mDb = OutletsLocalDatabase.getInstance(application)

    }



    override fun onInitViewModel() {
        viewModel.outletListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityVisible()
                    viewBinding.refreshLayout.isRefreshing = false
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.progressBar.setVisibilityGone()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        outletRecyclerAdapter.updateViewList(it.content)
                        if (!viewBinding.refreshLayout.isRefreshing) {
                            viewBinding.refreshLayout.isRefreshing = false
                        }

                        val regionalOffice = it.content.map {
                             RegionalOfficeFilterData(
                                    id = it.roid!!,
                                    name = it.regionaloffice!!,
                                    isSelected = false
                            ) }.toMutableList()
                        setRegionalOfficeTypeSpinner(regionalOffice)
                        val salesArea = it.content.map {
                            RegionalOfficeFilterData(
                                    id = it.said!!,
                                    name = it.salesarea!!,
                                    isSelected = false
                            )
                        }.toMutableList()
                        setSaleAreaTypeSpinner(salesArea)
                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    viewBinding.refreshLayout.isRefreshing = false
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        viewModel.getOutletData()

        viewModel.outletState.observe(this, Observer {
            when(it){
            Lce.Loading ->{
                viewBinding.errorLayout.root.setVisibilityGone()
                viewBinding.progressBar.setVisibilityVisible()
                viewBinding.refreshLayout.isRefreshing = false
                }
            is Lce.Content->
            {
                if (it.content.isEmpty()){
                    viewBinding.errorLayout.root.setVisibilityVisible()
                    viewBinding.progressBar.setVisibilityGone()
                    outletRecyclerAdapter.updateViewList(emptyList())
                    viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                    viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                    viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
                } else {
                    viewBinding.errorLayout.root.setVisibilityGone()
                    viewBinding.progressBar.setVisibilityGone()
                    outletRecyclerAdapter.updateViewList(it.content)
                    if (!viewBinding.refreshLayout.isRefreshing) {
                        viewBinding.refreshLayout.isRefreshing = false
                    }
                }
            }
            is Lce.Error ->
            {
                viewBinding.progressBar.setVisibilityGone()
                viewBinding.refreshLayout.isRefreshing = false
                viewBinding.progressBar.setVisibilityGone()
                showInformationDialog(it.error)

            }


        }
        })

    }


    private fun setSaleAreaTypeSpinner(salesArea: MutableList<RegionalOfficeFilterData>) {
        salesArea.sortBy { it.name }
        val setItems = salesArea.distinctBy { it.name }
        salesArea.clear()
        salesArea.addAll(setItems)


        salesspinner.setOnClickListener{
            SearchableMultiSelectSpinner.show(this, "Select Regional Office", "Done", salesArea, object :
                    SelectionCompleteListener {
                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
//                    Log.e("data", selectedItems.toString())
                    val ItemList = selectedItems

                    saleselectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
                  //  saleselectedItemList = selectedItemList.toString().stream().collect(Collectors.joining("','", "'", "'"))

                    saleselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }

                    salesspinner.text = saleselectedItemList
                    for (i in 0 until selectedItems.size){
                        val data = selectedItems[i]
                    }
                    val joiner = StringJoiner("','", "'", "'")
                    joiner.add("${selectedItems.toString().substring(1, selectedItems.toString().length - 1)}")

                    val stream = Stream.of(selectedItems.toString()).collect(Collectors.joining("','", "'", "'"))
                    Log.d("njabjj", stream)

                }

            })

        }

    }

    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems = regionalOffice.distinctBy { it.name }
        regionalOffice.clear()
        regionalOffice.addAll(setItems)


        regionalspinner.setOnClickListener{
            SearchableMultiSelectSpinner.show(this, "Select Regional Office", "Done", regionalOffice, object :
                    SelectionCompleteListener {
                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
//                    Log.e("data", selectedItems.toString())
//                    selectedItemList = selectedItems.toString()

                    selectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
                    roselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }
                    Log.d("nja", roselectedId)
                    regionalspinner.text = selectedItemList

                }

            })

        }

    }

    private fun initBottomsheetFileterView() {
        applyBtn.setOnClickListener {

            viewModel.getOutletsById(selectedItemList.toString(), saleselectedItemList.toString())
//            doAsync {
//                // Get the student list from database
//                val database = mDb.getOutletsDao().getOutletsByID(roselectedId, saleselectedId)
//
//                uiThread {
//                    toast("${database.size} records found.")
//                    // Display the students in text view
//                    outletRecyclerAdapter.updateViewList(database)
////
//                }
//            }




//            val cur = db.getDataUnique(roselectedId,saleselectedId)
//            if (cur.count > 0) {
////                if (cur.moveToFirst()) {
////                    do {
////                        filterOutletList.add(OutletsList(cur.getString(1)))
////                    } while (cur.moveToNext())
////                }
//                while (cur.moveToNext()) {
//                    Log.e("value", "" + cur.getString(1));
//                    filterOutletList.add(OutletsList(cur.getString(1)))
//                }
//                cur.close()
//                db.close()
//            }
//            val cu: Cursor = db.getData()
//            if (cu.count > 0) {
//                filterOutletList.clear()
//                while (cu.moveToNext()) {
//                    Log.e("value", "" + cu.getString(1));
//                    filterOutletList.add(OutletsList(cu.getString(1)))
//                }
//            }
//            outletRecyclerAdapter.updateViewList(database)
//            val cursor: Cursor = db.getListItem(roselectedId,saleselectedId)

//            if (cursor != null) {
//                cursor.moveToNext();
//
//                do {
////                    outletRecyclerAdapter.updateViewList(cursor.getString(1))
//                    Log.e("value", "" + cursor.getString(1));
//
//                } while (cursor.moveToNext());
//            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_outlet_filter, menu)
        val search = menu.findItem(R.id.action_search)
        var searchView = search.actionView as SearchView
        searchView = search.getActionView() as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                search.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //     sectionsPagerAdapter.fundSearchFilter(newText)
                outletRecyclerAdapter.getFilter().filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.complaints_action_filter -> {
                toggleFilterSheets()
                return true
            }
        }
        return true
    }
    private fun toggleFilterSheets() {

        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
//            if (newState == BottomSheetBehavior.STATE_HIDDEN)
//                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetDialog.show()
    }
    override fun OnOutletClick(outletsList: OutletsList) {
        Intent(this, OutletDetalisActivity::class.java).apply {
            putExtra(OutletDetalisActivity.OUTLET_ID, outletsList.outletid)
        }.also {
            startActivity(it)
        }
    }

}
