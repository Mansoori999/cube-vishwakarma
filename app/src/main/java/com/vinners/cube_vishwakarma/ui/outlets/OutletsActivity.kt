package com.vinners.cube_vishwakarma.ui.outlets


import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.devstune.searchablemultiselectspinner.SearchableAdapter
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
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.model.SalesAreaData
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import java.util.*
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

    var roselectedId = Arrays.asList<Int>()
    var saleselectedId = Arrays.asList<Int>()
    var selectedItemList:String? = ""
    var saleselectedItemList:String? = ""
    var roid:Int = 0
    private lateinit var resestTV: TextView
    var salesArea =  mutableListOf<OutletsList>()
    val regionalOfficeResetData =  mutableListOf<RegionalOfficeFilterData>()


    var searchableItems= listOf<RegionalOfficeFilterData>()
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

    private val multiValueSelectedSpinnerAdapter: SearchableAdapter by lazy {
        SearchableAdapter(
                this,
                searchableItems,
                searchableItems,
                object : SearchableAdapter.ItemClickListener {
                    override fun onItemClicked(
                            item: RegionalOfficeFilterData,
                            position: Int,
                            b: Boolean
                    ) {

                    }

                },false)
                .apply {
                    updateViewList(emptyList())
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
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }
            if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                viewModel.getOutletsByIdWithOR(roselectedId, saleselectedId)
            } else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                viewModel.getOutletsById(roselectedId, saleselectedId)

            } else{
                viewModel.getOutletData()
            }

        }
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
        resestTV = bottomSheetView.findViewById(R.id.resestTV)
        initBottomsheetFileterView()
//        mDb = OutletsLocalDatabase.getInstance(application)

    }



    override fun onInitViewModel() {
//        viewModel.loadRegionalAndSalesInfo
//                .observe(this, Observer {
//                    when (it) {
//                        LoadMetaForAddOutletState.LoadingLoadMetaForAddOutletState -> {
//                            viewBinding.errorLayout.root.setVisibilityGone()
//                            viewBinding.progressBar.setVisibilityVisible()
//                            viewBinding.refreshLayout.isRefreshing = false
//                        }
//                        is LoadMetaForAddOutletState.MetaForAddOutletStateLoaded->{
//                            if (it.regionalAndSalesInfo.isEmpty()){
//                                viewBinding.errorLayout.root.setVisibilityVisible()
//                                viewBinding.progressBar.setVisibilityGone()
//                                if (!viewBinding.refreshLayout.isRefreshing) {
//                                    viewBinding.refreshLayout.isRefreshing = false
//                                }
//                                viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
//                                viewBinding.errorLayout.errorActionButton.setVisibilityGone()
//                                viewBinding.errorLayout.messageTv.text = "Not Outlets Found"
//                            } else {
//                                viewBinding.errorLayout.root.setVisibilityGone()
//                                viewBinding.progressBar.setVisibilityGone()
//                                outletRecyclerAdapter.updateViewList(it.regionalAndSalesInfo)
//                                if (!viewBinding.refreshLayout.isRefreshing) {
//                                    viewBinding.refreshLayout.isRefreshing = false
//                                }
//
//                                val regionalOffice = it.regionalAndSalesInfo.map {
//                                    RegionalOfficeFilterData(
//                                            id = it.roid!!,
//                                            name = it.regionaloffice!!,
//                                            isSelected = false
//                                    ) }.toMutableList()
//                                setRegionalOfficeTypeSpinner(regionalOffice)
//                                val salesArea = it.regionalAndSalesInfo.map {
//                                    RegionalOfficeFilterData(
//                                            id = it.said!!,
//                                            name = it.salesarea!!,
//                                            isSelected = false
//                                    )
//                                }.toMutableList()
//                                setSaleAreaTypeSpinner(salesArea)
//                            }
//
//                        }
//                        is LoadMetaForAddOutletState.ErrorInLoadingMetaForAddOutletLoaded->{
//                            viewBinding.progressBar.setVisibilityGone()
//                            viewBinding.refreshLayout.isRefreshing = false
//                            viewBinding.progressBar.setVisibilityGone()
//                            showInformationDialog(it.error)
//                        }
//
//                    }
//
//                })
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
                        if (!viewBinding.refreshLayout.isRefreshing) {
                            viewBinding.refreshLayout.isRefreshing = false
                        }
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
                        salesArea.addAll(it.content)
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
        viewModel.outletStateWithOR.observe(this, Observer {
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


//    private fun setSaleAreaTypeSpinner(salesArea: MutableList<RegionalOfficeFilterData>) {
//        salesArea.sortBy { it.name }
//        val setItems = salesArea.distinctBy { it.name }
//        salesArea.clear()
//        salesArea.addAll(setItems)
//
//
//        salesspinner.setOnClickListener{
//            SearchableMultiSelectSpinner.show(this, "Select Sales Area", "Done", salesArea, object :
//                    SelectionCompleteListener {
//                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
////                    Log.e("data", selectedItems.toString())
//                    val ItemList = selectedItems
//
//                    saleselectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
//
//                    saleselectedId = selectedItems.map { it.id }
//
////                    saleselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }
//
//                    salesspinner.text = saleselectedItemList
//
//                }
//
//            })
//
//        }
//
//    }

    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems = regionalOffice.distinctBy { it.name }
        regionalOffice.clear()
        regionalOffice.addAll(setItems)


//        regionalspinner.setOnClickListener{
//            SearchableMultiSelectSpinner.show(this, "Select Regional Office", "Done", regionalOffice, object :
//                    SelectionCompleteListener {
//                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
//                    selectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
////                   roselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }
//                    roselectedId  = selectedItems.map { it.id }
//                    regionalspinner.text = selectedItemList
//                    setSalesOnSpinner(roselectedId)
//
//                }
//
//            })
//
//        }
        regionalspinner.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val convertView = inflater.inflate(R.layout.searchable_list_layout, null)

            alertDialog.setView(convertView)
            alertDialog.setTitle("Select Regional Office")

            val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            val recyclerView =
                convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
            val mLayoutManager = LinearLayoutManager(this)

            val adapter =
                SearchableAdapter(
                    this,
                    regionalOffice,
                    regionalOffice,
                    object : SearchableAdapter.ItemClickListener {
                        override fun onItemClicked(
                            item: RegionalOfficeFilterData,
                            position: Int,
                            b: Boolean
                        ) {
                            for (i in 0 until regionalOffice.size) {
                                if (regionalOffice[i].id == item.id) {
                                    regionalOffice[i].isSelected = b
                                    break
                                }
                            }
                        }

                    }, false)

            adapter.notifyDataSetChanged()


            recyclerView.itemAnimator = null
            recyclerView.layoutManager = mLayoutManager
            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // do something on text submit
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // do something when text changes
                    adapter.filter.filter(newText)
                    return false
                }
            })

            alertDialog.setPositiveButton("Done") { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList = ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until regionalOffice.size) {
                    if (regionalOffice[i].isSelected) {
                        resultList.add(regionalOffice[i])
                    }
                }
                regionalspinner.text = resultList.toString().substring(1, resultList.toString().length - 1)
                roselectedId  = resultList.map { it.id }
                regionalOfficeResetData.addAll(resultList)
                setSalesOnSpinner(roselectedId)
//                selectionCompleteListener?.onCompleteSelection(resultList)
            }
            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until regionalOffice.size) {
                    if (regionalOffice[i].isSelected) {
                        regionalOffice[i].isSelected = false
                        resultList.remove(regionalOffice[i])
                    }
                }
                regionalspinner.text = ""
//                selectionCompleteListener?.onCompleteSelection(resultList)
            }

            alertDialog.show()
        }



    }

    private fun setSalesOnSpinner(id: List<Int>) {

        for(i in 0 until id.size){
             roid = id[i]
        }
        val sales = salesArea.filter {
           it.roid == roid
        }.map {
            RegionalOfficeFilterData(
                    it.said!!,
                    it.salesarea!!,
                    isSelected = false
            )
        }.distinctBy {
            it.id
        }.toMutableList()
        sales.sortBy { it.name }
        val setItems = sales.distinctBy { it.name }
        sales.clear()
        sales.addAll(setItems)


//        salesspinner.setOnClickListener{
//            SearchableMultiSelectSpinner.show(this, "Select Sales Area", "Done", sales, object :
//                    SelectionCompleteListener {
//                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
////                    Log.e("data", selectedItems.toString())
//                    val ItemList = selectedItems
//
//                    saleselectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
//
//                    saleselectedId = selectedItems.map { it.id }
//
////                    saleselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }
//
//                    salesspinner.text = saleselectedItemList
//                }
//            })
//        }
        salesspinner.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val convertView = inflater.inflate(R.layout.searchable_list_layout, null)

            alertDialog.setView(convertView)
            alertDialog.setTitle("Select Sales Area")

            val searchView = convertView.findViewById<SearchView>(R.id.searchView)
            val recyclerView =
                convertView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
            val mLayoutManager = LinearLayoutManager(this)

            val adapter =
                SearchableAdapter(
                    this,
                    sales,
                    sales,
                    object : SearchableAdapter.ItemClickListener {
                        override fun onItemClicked(
                            item: RegionalOfficeFilterData,
                            position: Int,
                            b: Boolean
                        ) {
                            for (i in 0 until sales.size) {
                                if (sales[i].id == item.id) {
                                    sales[i].isSelected = b
                                    break
                                }
                            }
                        }

                    }, false)

            adapter.notifyDataSetChanged()


            recyclerView.itemAnimator = null
            recyclerView.layoutManager = mLayoutManager
            recyclerView.adapter = adapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    // do something on text submit
                    return false
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    // do something when text changes
                    adapter.filter.filter(newText)
                    return false
                }
            })

            alertDialog.setPositiveButton("Done") { dialogInterface, i ->
                dialogInterface.dismiss()
                val resultList = ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until sales.size) {
                    if (sales[i].isSelected) {
                        resultList.add(sales[i])
                    }
                }
                salesspinner.text = resultList.toString().substring(1, resultList.toString().length - 1)
                saleselectedId  = resultList.map { it.id }

//                selectionCompleteListener?.onCompleteSelection(resultList)
            }
            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
                val resultList =ArrayList<RegionalOfficeFilterData>()
                for (i in 0 until sales.size) {
                    if (sales[i].isSelected) {
                        sales[i].isSelected = false
                        resultList.remove(sales[i])
                    }
                }
                salesspinner.text = ""
//                selectionCompleteListener?.onCompleteSelection(resultList)
            }

            alertDialog.show()

        }


        resestTV.setOnClickListener {
            val resultList = ArrayList<RegionalOfficeFilterData>()
            for (i in 0 until regionalOfficeResetData.size) {
                if (regionalOfficeResetData[i].isSelected) {
                    regionalOfficeResetData[i].isSelected = false
                    resultList.remove(regionalOfficeResetData[i])
                    regionalspinner.setText("")
                }
            }
            roselectedId.clear()
            saleselectedId.clear()
            val result =ArrayList<RegionalOfficeFilterData>()
            for (i in 0 until sales.size) {
                if (sales[i].isSelected) {
                    sales[i].isSelected = false
                    result.remove(sales[i])
                    salesspinner.text = ""
                }
            }


        }
    }


    private fun initBottomsheetFileterView() {
        applyBtn.setOnClickListener {
            if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                viewModel.getOutletsByIdWithOR(roselectedId, saleselectedId)
            }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                viewModel.getOutletsById(roselectedId, saleselectedId)
            }else if (roselectedId.isEmpty() && saleselectedId.isEmpty()){
                viewModel.getOutletData()
            }
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            bottomSheetDialog.dismiss()


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
