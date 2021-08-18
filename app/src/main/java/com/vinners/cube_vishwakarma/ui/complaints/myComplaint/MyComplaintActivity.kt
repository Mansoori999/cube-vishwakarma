package com.vinners.cube_vishwakarma.ui.complaints.myComplaint

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.devstune.searchablemultiselectspinner.SearchableAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.onItemSelected
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*

import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.MyComplaintSharedViewModel
import com.vinners.cube_vishwakarma.ui.dashboardFilter.ActiveSubAdminData
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import java.util.*
import javax.inject.Inject

class MyComplaintActivity: BaseActivity<ActivityMyComplaintBinding,AllComplaintFragmentViewModel>(R.layout.activity_my_complaint) {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private val sectionsPagerAdapter: MyComplaintPagerAdapter by lazy {
        MyComplaintPagerAdapter(supportFragmentManager)

    }
    private lateinit var applyBtn: TextView
    var userid : String? = null
    var adminUserid : String = ""
    private lateinit var regionalspinner: TextView
    private lateinit var salesspinner: TextView
    private lateinit var subadminSpinner:Spinner
    private lateinit var activeSubadminLayout:LinearLayout
    private lateinit var resestTV: TextView
    var salesArea =  mutableListOf<MyComplaintList>()
    var roselectedId = Arrays.asList<Int>()
    var saleselectedId = Arrays.asList<Int>()
    var subadminId = Arrays.asList<Int>()
    var selectedItemList:String? = ""
    var saleselectedItemList:String? = ""
    var roid:Int = 0
    val regionalOfficeResetData =  mutableListOf<RegionalOfficeFilterData>()


    var searchableItems= listOf<RegionalOfficeFilterData>()
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    private lateinit var bottomSheetDialog: BottomSheetDialog

//    @Inject
//    lateinit var sharedViewModel: MyComplaintSharedViewModel

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: AllComplaintFragmentViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }


    private fun setUpViewPager() {
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
    override fun onInitViewModel() {

        viewModel.complaintListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()

                }
                is Lce.Content->
                {
                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)
                        tabs.setVisibilityVisible()
                        viewPager.setVisibilityVisible()
                        viewPager.adapter = sectionsPagerAdapter
                        tabs.setupWithViewPager(viewPager)
                        val regionalOffice = it.content.map {
                            RegionalOfficeFilterData(
                                    id = it.roid!!,
                                    name = it.regionaloffice!!,
                                    isSelected = false
                            ) }.toMutableList()
                        setRegionalOfficeTypeSpinner(regionalOffice)
                        salesArea.addAll(it.content)

                        val activeSubadmin = it.content.map {
                            ActiveSubAdminData(
                                    id = it.subadminid!!,
                                    name = it.subadmin!!,

                                    )
                        }.toMutableList()
                        setActiveSubAdminTypeSpinner(activeSubadmin)

                    }

                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        if (userSessionManager.designation!!.toLowerCase().equals("admin")){
            viewModel.getComplaintList(adminUserid)
        }else{
            viewModel.getComplaintList(userid!!)

        }
        viewModel.complaintDaoROANDSaidListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        viewModel.complaintDaoROORSaidListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        viewModel.complaintDaoAllANDListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })

        viewModel.complaintDaoWithSubAminORListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })
        viewModel.complaintDaoWithSubadminAndROListState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.progressBar.setVisibilityVisible()
                }
                is Lce.Content->
                {

                    if (it.content.isEmpty()){
                        viewBinding.progressBar.setVisibilityGone()

                    } else {
                        viewBinding.progressBar.setVisibilityGone()
                        viewModel.updateData(it.content)

                    }
                }
                is Lce.Error ->
                {
                    viewBinding.progressBar.setVisibilityGone()
                    showInformationDialog(it.error)

                }


            }
        })


    }

    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems = regionalOffice.distinctBy { it.name }
        regionalOffice.clear()
        regionalOffice.addAll(setItems)

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

    private fun setActiveSubAdminTypeSpinner(activeSubadmin: MutableList<ActiveSubAdminData>) {
        activeSubadmin.sortBy { it.name }
        val setItems = activeSubadmin.distinctBy { it.name }
        activeSubadmin.clear()
        activeSubadmin.addAll(setItems)
        activeSubadmin.add(
                0,
                ActiveSubAdminData(
                        id = "XX",
                        name = "Select SubAdmin"
                )
        )
        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                activeSubadmin
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(subadminSpinner) {
            adapter = aa
            prompt = "Select SubAdmin"
            gravity = android.view.Gravity.CENTER
        }

    }

    override fun onInitDataBinding() {
        userid = userSessionManager.userId
        setSupportActionBar(viewBinding.mycomplaintToolbar)
        getSupportActionBar()!!.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_left,null))
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        viewBinding.mycomplaintToolbar.setNavigationOnClickListener { view -> onBackPressed() }

        viewBinding.refresh.setOnClickListener {
            if (userSessionManager.designation!!.toLowerCase().equals("admin")){
                viewPager.setVisibilityVisible()
                viewModel.getComplaintList(adminUserid)
            }else{
                viewModel.getComplaintList(userid!!)

            }
        }
        viewBinding.filter.setOnClickListener {
            toggleFilterSheets()
        }
        tabs = findViewById(R.id.tabs)
        // val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        viewPager = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        dotsIndicator.setViewPager(viewPager)
//        setUpViewPager()
        val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, p1: Int) {}
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_filter_outlet_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        regionalspinner = bottomSheetView.findViewById(R.id.ro_spinner)
        salesspinner = bottomSheetView.findViewById(R.id.sales_spinner)
       subadminSpinner = bottomSheetView.findViewById(R.id.subadmin_spinner)
        activeSubadminLayout = bottomSheetView.findViewById(R.id.activeSubadmin_layout)
        activeSubadminLayout.setVisibilityVisible()
        sheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior.setBottomSheetCallback(bottomSheetCallback)
        applyBtn = bottomSheetView.findViewById(R.id.apply_textview)
        resestTV = bottomSheetView.findViewById(R.id.resestTV)
        initBottomsheetFileterView()

        subadminSpinner.onItemSelected { adapterView, view, i, l ->
            if (subadminSpinner.childCount != 0 && subadminSpinner.selectedItemPosition != 0) {
                val subadminData = subadminSpinner.selectedItem as ActiveSubAdminData
                subadminId.add(subadminData.id.toInt())
            }
        }
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
    private fun initBottomsheetFileterView() {
        applyBtn.setOnClickListener {
            if (roselectedId.isEmpty().not() && saleselectedId.isEmpty()){
                viewModel.getComplaintBYIDROANDSaid(roselectedId, saleselectedId)
            }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not()) {
                viewModel.getComplaintBYIDWithOR(roselectedId, saleselectedId)
            }else if (roselectedId.isEmpty().not() && saleselectedId.isEmpty().not() && subadminId.isEmpty().not()){
                viewModel.getComplaintByAllIDAND(roselectedId, saleselectedId,subadminId)
            }else if (roselectedId.isEmpty().not() || saleselectedId.isEmpty().not() || subadminId.isEmpty().not()){
                viewModel.getComplaintByIDWithSubAminOR(roselectedId, saleselectedId,subadminId)
            }else if (roselectedId.isEmpty().not() && subadminId.isEmpty().not()){
                viewModel.getComplaintByIDWithSubadminAndRO(roselectedId,subadminId)
            }else{
                if (userSessionManager.designation!!.toLowerCase().equals("admin")){
                    viewModel.getComplaintList(adminUserid)
                }else{
                    viewModel.getComplaintList(userid!!)

                }
            }
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            bottomSheetDialog.dismiss()


        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.complaint_search_menu, menu)
        val search = menu.findItem(R.id.action_search)
//        val refresh = menu.findItem(R.id.action_refresh)
//        refresh.setOnMenuItemClickListener(this)
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
                when (viewPager.currentItem) {
                    0 -> {
                        val fragment =  viewPager.adapter!!.instantiateItem(viewPager, 0) as Fragment
                        if ( fragment is AllFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    1 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 1) as Fragment
                        if (fragment is DueFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    2 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 2) as Fragment
                        if (fragment is WorkingFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    3 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 3) as Fragment
                        if (fragment is HoldFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    4 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 4) as Fragment
                        if (fragment is CancelledFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    5 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 5) as Fragment
                        if (fragment is DoneFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    6 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 6) as Fragment
                        if (fragment is DraftFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    7 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 7) as Fragment
                        if (fragment is EstimatedFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    8 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 8) as Fragment
                        if (fragment is BilledFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    9 -> {
                        val fragment = viewPager.adapter!!.instantiateItem(viewPager, 9) as Fragment
                        if (fragment is PaymentFragment)
                            fragment.allComplaintSearchFilter(newText)
                        return true
                    }
                    else -> return false
                }

            }
        })
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onMenuItemClick(item: MenuItem?): Boolean {
//        if (userSessionManager.designation!!.toLowerCase().equals("admin")){
//                    viewModel.getComplaintList(adminUserid)
//                }else{
//                    viewModel.getComplaintList(userid!!)
//
//                }
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.action_refresh -> {
//                if (userSessionManager.designation!!.toLowerCase().equals("admin")){
//                    viewModel.getComplaintList(adminUserid)
//                }else{
//                    viewModel.getComplaintList(userid!!)
//
//                }

//                return true
//            }
//           R.id.action_search ->{
//
//           }
//        }
//        return true
//
//    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        // manually clear store
//        clearViewModel()
//    }
}