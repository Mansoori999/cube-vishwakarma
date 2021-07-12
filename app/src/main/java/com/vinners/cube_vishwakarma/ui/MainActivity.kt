package com.vinners.cube_vishwakarma.ui


import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import coil.api.load
import com.devstune.searchablemultiselectspinner.SearchableAdapter
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import com.vinners.cube_vishwakarma.BuildConfig
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.onItemSelected
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.homeScreen.MainActivityListModel
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMainBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.feature_auth.ui.AuthActivity
import com.vinners.cube_vishwakarma.ui.attendance.AttendanceActivity
import com.vinners.cube_vishwakarma.ui.complaints.ComplaintsActivity
import com.vinners.cube_vishwakarma.ui.dashboardFilter.ActiveSubAdminData
import com.vinners.cube_vishwakarma.ui.dashboardFilter.FinancialYearData
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import com.vinners.cube_vishwakarma.ui.documents.DocumentsActivity
import com.vinners.cube_vishwakarma.ui.expense.ExpenseActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletComplaintsActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletsActivity
import com.vinners.cube_vishwakarma.ui.profile.ProfileActivity
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : BaseActivity<ActivityMainBinding , MainActivityViewModel>(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener,MainActivityRecyclerAdapter.ClickListener {
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    var list: MutableList<String> = ArrayList()
    var searchableItems = listOf<RegionalOfficeFilterData>()

    private var selectionCompleteListener: SelectionCompleteListener? = null
    private lateinit var bottomSheetDialog: BottomSheetDialog


    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    var afterResetRO = mutableListOf<RegionalOfficeFilterData>()
    private lateinit var financialspinner: SearchableSpinner
    private lateinit var regionalspinner: TextView
    private lateinit var applyBtn: TextView
    private lateinit var resestTV: TextView
    private lateinit var subadminSpinner: SearchableSpinner
//    var afterResetRO = listOf<RegionalOfficeFilterData>()

    var roselectedId: String? = null

    var dataEnteriesYAxis = listOf<Int>()
    var monthlyDataXAxis = listOf<String>()

    var roDataEnteriesYAxis = listOf<Int>()
    var roListXAxis = listOf<String>()


    lateinit var defaultStartDate: String
    lateinit var defaultEndDate: String
    lateinit var startDateF: String
    lateinit var endDateF: String
    var startdate: String? = null
    var enddate: String? = null
    var startdatef: String? = null
    var enddatef: String? = null
    var regionalOfficeIds: String? = null
    var fyearDateId: Int? = null
    var fyearDateName: String? = null
    var roIds: String? = null
    val isClicked: Boolean = false
    var subadminId :String? = null

    @Inject
    lateinit var appInfo: AppInfo

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private val sectionsPagerAdapter: DashboardPagerAdapter by lazy {
        DashboardPagerAdapter(supportFragmentManager)

    }
    private val homeList = ArrayList<MainActivityListModel>()

    private val mainActivityRecyclerAdapter: MainActivityRecyclerAdapter by lazy {
        MainActivityRecyclerAdapter(this)
                .apply {
                    updateViewList(homeList = emptyList())
                }
    }

    companion object {
        const val MONTHLY = "monthly"
        const val SUMMARY = "summary"
        const val ROWISE = "rowise"

    }
    private var tabPosition: Int? = 0
    private var selectedTab: String? = null
    override val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {
//        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
//        viewPager = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        viewPager.setCurrentItem(1)
//        dotsIndicator.setViewPager(viewPager)


        viewBinding.contentMainContainer.tabs.addTab(viewBinding.contentMainContainer.tabs.newTab().setText("Monthly"))
        viewBinding.contentMainContainer.tabs.addTab(viewBinding.contentMainContainer.tabs.newTab().setText("Summary"))
        viewBinding.contentMainContainer.tabs.addTab(viewBinding.contentMainContainer.tabs.newTab().setText("RO Wise"))

        val tab = findViewById<TabLayout>(R.id.tabs)
        tab.selectTab(tab.getTabAt(1))
        if (selectedTab.equals(null)){
            viewBinding.contentMainContainer.summaryContainer.setVisibilityVisible()
        }
        viewBinding.contentMainContainer.tabs.addOnTabSelectedListener(onTabSelectedListener)
        val hiuserTV = findViewById<TextView>(R.id.hiuserTV)
        hiuserTV.setText(String.format("Hii, %s", userSessionManager.userName))
        val hiuserMobileTV = findViewById<TextView>(R.id.hiuserMobileTV)

        viewBinding.contentMainContainer.profilePic.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        viewBinding.contentMainContainer.profileContainer.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        hiuserMobileTV.text = userSessionManager.mobile
//        setProfilePicture()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val drawermenu: ImageView = findViewById(R.id.drawer_menu)
        drawermenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val toggle =
                ActionBarDrawerToggle(
                        this, drawerLayout,
                        R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close
                )
        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        navigationView.setItemIconTintList(null)
        val userName = headerView.findViewById<TextView>(R.id.username)
        userName.setText(String.format("Hii, %s", userSessionManager.userName))
        val userMobile = headerView.findViewById<TextView>(R.id.userMobile)
        userMobile.text = userSessionManager.mobile
        val appVersionTV = findViewById<TextView>(R.id.appVersionTV)
        appVersionTV.text = "App Version: ${BuildConfig.VERSION_NAME}"
        navigationView.setNavigationItemSelectedListener(this)

//        val inflater: LayoutInflater = this@MainActivity.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val viewGroup : ViewGroup = findViewById (R.id.nav_view)
//        var view = inflater.inflate(R.layout.nav_header_main, viewGroup)
//        val userName = view.findViewById<TextView>(R.id.username)
//        userName.setText(String.format("Hii, %s", userSessionManager.userName))
//        val userMobile = view.findViewById<TextView>(R.id.userMobile)
//        userMobile.text = userSessionManager.mobile
//        val appVersionTV = view.findViewById<TextView>(R.id.appVersionTV)
//        appVersionTV.text = "App Version: ${BuildConfig.VERSION_NAME}"

//        val complaints = view.findViewById<CardView>(R.id.complaint_container)
//        val documents = view.findViewById<CardView>(R.id.doc_container)
//        val attendance = view.findViewById<CardView>(R.id.attendance_container)
//        val expense = view.findViewById<CardView>(R.id.expense_container)
//        val tutorials = view.findViewById<CardView>(R.id.tutorial_container)
//        val outlets = view.findViewById<CardView>(R.id.outlets_container)

//
//        complaints.setOnClickListener {
//            complaints.startAnimation(myAnim)
//            val intent = Intent(this, ComplaintsActivity::class.java)
//            startActivity(intent)
//        }
//        documents.setOnClickListener {
//            documents.startAnimation(myAnim)
//            val intent = Intent(this, DocumentsActivity::class.java)
//            startActivity(intent)
//        }
//        attendance.setOnClickListener {
//            attendance.startAnimation(myAnim)
//            val intent = Intent(this, AttendanceActivity::class.java)
//            startActivity(intent)
//        }
//        expense.setOnClickListener {
//            expense.startAnimation(myAnim)
//            val intent = Intent(this, ExpenseActivity::class.java)
//            startActivity(intent)
//        }
//        tutorials.setOnClickListener {
//            tutorials.startAnimation(myAnim)
//            val intent = Intent(this, TutorialsActivity::class.java)
//            startActivity(intent)
//        }
//        outlets.setOnClickListener {
//            outlets.startAnimation(myAnim)
//            val intent = Intent(this, OutletsActivity::class.java)
//            startActivity(intent)
//        }
//        val logoutBtn = findViewById<ImageView>(R.id.logout)
//        logoutBtn.setOnClickListener {
//            MaterialAlertDialogBuilder(this)
//                .setTitle("Log Out")
//                .setMessage("Do you want to log out?")
//                .setPositiveButton("Yes") { _, _ ->
////                        userSessionManager.logOut()
//                    viewModel.logout()
//                }.setNegativeButton("No") { dialog, _ ->
//                    dialog.cancel()
//                }.show()
//        }
//        val recyclerView: RecyclerView = findViewById(R.id.Recyclerview_home)
//
//        val layoutManager = GridLayoutManager(this, 2)
//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                val pos = position % 5
//                if (pos == 0){
//                    return 2
//                }else{
//                    return 1
//                }
//            }
//        }
//        recyclerView.layoutManager = layoutManager
//        mainActivityRecyclerAdapter.updateViewList(homeList)
//        recyclerView.adapter = mainActivityRecyclerAdapter
//        preparehomeData()


        startDateF = startdatef.toString()
        endDateF = enddatef.toString()
        val myAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.card_anim)
        viewBinding.contentMainContainer.totalCardView.setOnClickListener {
            viewBinding.contentMainContainer.totalCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_TOTAL_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.dueCardView.setOnClickListener {
            viewBinding.contentMainContainer.dueCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DUE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.workingCardView.setOnClickListener {
            viewBinding.contentMainContainer.workingCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_WORKING_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.pendingCardView.setOnClickListener {
            viewBinding.contentMainContainer.pendingCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PENDING_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }

        viewBinding.contentMainContainer.doneCardView.setOnClickListener {
            viewBinding.contentMainContainer.doneCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DONE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }

        viewBinding.contentMainContainer.draftCardView.setOnClickListener {
            viewBinding.contentMainContainer.draftCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DRAFT_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.estimatedCardView.setOnClickListener {
            viewBinding.contentMainContainer.estimatedCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_ESTIMATE_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.billedCardView.setOnClickListener {
            viewBinding.contentMainContainer.billedCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_BILLED_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.paymentCardView.setOnClickListener {
            viewBinding.contentMainContainer.paymentCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PAYMENT_ACTIVITY, true)
            intent.putExtra("startDate", defaultStartDate)
            intent.putExtra("endDate", defaultEndDate)
            intent.putExtra("startDateF", startDateF)
            intent.putExtra("endDateF", endDateF)
            intent.putExtra("regionalOfficeId", regionalOfficeIds)
            intent.putExtra("regionalOfficeId", roselectedId)
            intent.putExtra("subadminId",subadminId)
            startActivity(intent)

        }
//        sheetBehavior = BottomSheetBehavior.from(complaints_filter_bottomsheet)
//        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        val bottomSheetDialog = BottomSheetDialog(this)
//        bottomSheetDialog.setContentView(bottomSheetView)
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, p1: Int) {}
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_filter_layout, null)
        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        financialspinner = bottomSheetView.findViewById(R.id.financial_spinner)
        regionalspinner = bottomSheetView.findViewById(R.id.ro_spinner)
        subadminSpinner = bottomSheetView.findViewById(R.id.subadmin_spinner)
        val activeSubadminlayout = bottomSheetView.findViewById<LinearLayout>(R.id.activeSubadmin_layout)
        if (userSessionManager.designation?.toLowerCase().equals("admin")) {
            activeSubadminlayout.setVisibilityVisible()
        } else {
            activeSubadminlayout.setVisibilityGone()
        }
        regionalspinner.setHintTextColor(getResources().getColor(R.color.black))
        sheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        sheetBehavior.setBottomSheetCallback(bottomSheetCallback)
        viewBinding.contentMainContainer.filter.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
            }
//            if (newState == BottomSheetBehavior.STATE_HIDDEN)
//                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.show()

        }

        applyBtn = bottomSheetView.findViewById(R.id.apply_textview)
        resestTV = bottomSheetView.findViewById(R.id.resestTV)

        subadminSpinner.onItemSelected { adapterView, view, i, l ->
            if (subadminSpinner.childCount != 0 && subadminSpinner.selectedItemPosition != 0) {
                val subadminData = subadminSpinner.selectedItem as ActiveSubAdminData
                subadminId = subadminData.id
            }
        }
        initBottomsheetFileterView()
        viewBinding.contentMainContainer.refresh.setOnClickListener {
            viewModel.initViewModel()
            if (startDateF.equals("null").not() && endDateF.equals("null").not()){
                viewModel.dashBoardData(startDateF, endDateF, roselectedId,subadminId)
            }else {
                viewModel.dashBoardData(defaultStartDate, defaultEndDate, regionalOfficeIds,subadminId)

            }

        }

    }



    private fun preparehomeData() {
        homeList.add(MainActivityListModel("Totals", "#99CC33"))
        homeList.add(MainActivityListModel("Due", "#FF6633"))
        homeList.add(MainActivityListModel("Working", "#0066CC"))
        homeList.add(MainActivityListModel("Pending Letter", "#2EC43E"))
        homeList.add(MainActivityListModel("Done", "#ff0000"))

        mainActivityRecyclerAdapter.notifyDataSetChanged()
    }

    override fun onInitViewModel() {
        viewModel.logoutState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    Toast.makeText(this, "LogOut Successfully", Toast.LENGTH_SHORT).show()
                    val i = Intent(this@MainActivity, AuthActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                    finish()
                }
                is Lse.Error -> {

                }
            }
        })
        viewModel.profile.observe(this, Observer {
            val profilepic = findViewById<CircleImageView>(R.id.profile_pic)
            if (it.pic.isNullOrEmpty().not())
                profilepic.load(appInfo.getFullAttachmentUrl(it.pic!!))
            else
                profilepic.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.user, null))

        })
        viewModel.initViewModel()

        viewModel.financialFilterState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                }
                is Lce.Content -> {

                    val financialdata = it.content.map {
                        FinancialYearData(
                                id = it.id,
                                name = it.name,
                                startdate = it.startdate!!,
                                enddate = it.enddate!!
                        )
                    }.toMutableList()
                    setFinancialYearTypeSpinner(financialdata)

                }
                is Lce.Error -> {
                }
            }
        })
        viewModel.getRinancialYearFilterData()

        viewModel.dashboardState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.contentMainContainer.loadingData.setVisibilityVisible()
                }
                is Lce.Content -> {
                    it.content.dashboardCount.forEach{
                        viewBinding.contentMainContainer.loadingData.setVisibilityGone()
                        viewBinding.contentMainContainer.totals.text = it.total
                        viewBinding.contentMainContainer.due.text = it.due
                        viewBinding.contentMainContainer.working.text = it.working
                        viewBinding.contentMainContainer.pending.text = it.pendingletter
                        viewBinding.contentMainContainer.done.text = it.done
                        viewBinding.contentMainContainer.draft.text = it.draft
                        viewBinding.contentMainContainer.estimated.text = it.estimated
                        viewBinding.contentMainContainer.billed.text = it.billed
                        viewBinding.contentMainContainer.payment.text = it.payment

                    }
                    dataEnteriesYAxis =  it.content.monthWiseChart!!.data.toList()
                    monthlyDataXAxis = it.content.monthWiseChart!!.monthList
                    roDataEnteriesYAxis = it.content.roWiseChart!!.data.toList()
                    roListXAxis = it.content.roWiseChart!!.roList
                    monthWiseBarChart()
                    roWiseBarChart()
                }
                is Lce.Error -> {
                    viewBinding.contentMainContainer.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })

        viewModel.regionalOfficeFilterState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                }
                is Lce.Content -> {
                    val regionalOffice = it.content.map {
                        RegionalOfficeFilterData(
                                id = it.roid!!,
                                name = it.regionaloffice!!,
                                isSelected = false
                        )
                    }.toMutableList()
                    setRegionalOfficeTypeSpinner(regionalOffice)

                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.getRegionalOfficeFilterData()
        viewModel.acitveSubAdminFilterState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {
                }
                is Lce.Content -> {

                    val activeSubadmin = it.content.map {
                        ActiveSubAdminData(
                                id = it.id!!,
                                name = it.name!!,

                                )
                    }.toMutableList()
                    setActiveSubAdminTypeSpinner(activeSubadmin)

                }
                is Lce.Error -> {
                }
            }
        })
        viewModel.activeSubAdmib()

    }
    private fun roWiseBarChart() {
        val roBarEntries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until roDataEnteriesYAxis.size) {
            roBarEntries.add(BarEntry(i.toFloat(), roDataEnteriesYAxis[i].toFloat()))
        }

        val roBarDataSet = BarDataSet(roBarEntries,"Complaint")
        roBarDataSet.setColor(Color.parseColor("#2383E1"))
        roBarDataSet.valueTextColor = Color.BLACK
        roBarDataSet.setFormSize(15f)
        viewBinding.contentMainContainer.barChartRO.getDescription().setEnabled(false)
        val xAxisro = viewBinding.contentMainContainer.barChartRO.getXAxis()
        xAxisro.granularity = 1f
        xAxisro.isGranularityEnabled = true
        xAxisro.setDrawGridLines(false)
        xAxisro.setCenterAxisLabels(false)
        xAxisro.axisMaximum = roListXAxis.size.toFloat()
        xAxisro.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxisro.valueFormatter = IndexAxisValueFormatter(roListXAxis)
        val roBarData = BarData(roBarDataSet)
        viewBinding.contentMainContainer.barChartRO.setFitBars(true)
        viewBinding.contentMainContainer.barChartRO.setDragEnabled(true)
        viewBinding.contentMainContainer.barChartRO.setData(roBarData)
        roBarData.barWidth = 0.5f
        viewBinding.contentMainContainer.barChartRO.setExtraOffsets(5f,5f,5f,15f)
        //Y-axis
        viewBinding.contentMainContainer.barChartRO.getAxisRight().setEnabled(false)
        val leftAxisro: YAxis = viewBinding.contentMainContainer.barChartRO.getAxisLeft()
        leftAxisro.setDrawGridLines(true)
//        leftAxisro.setSpaceTop(15f)
        leftAxisro.setAxisMinimum(0f)
        val yAxisRight: YAxis = viewBinding.contentMainContainer.barChartRO.getAxisRight()
        yAxisRight.setEnabled(false)
        viewBinding.contentMainContainer.barChart.setExtraBottomOffset(1f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMaximum(12f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMinimum(4f)
        viewBinding.contentMainContainer.barChartRO.invalidate()

    }

    private fun monthWiseBarChart(){
        val barEntries: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until dataEnteriesYAxis.size) {
            barEntries.add(BarEntry(i.toFloat(), dataEnteriesYAxis[i].toFloat()))
        }

        val barDataSet = BarDataSet(barEntries,"Complaint")
        barDataSet.setColor(Color.parseColor("#2383E1"))
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.setFormSize(15f)
        viewBinding.contentMainContainer.barChart.getDescription().setEnabled(false)
        val xAxis = viewBinding.contentMainContainer.barChart.getXAxis()
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.setCenterAxisLabels(false)
        xAxis.axisMaximum = monthlyDataXAxis.size.toFloat()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.valueFormatter = IndexAxisValueFormatter(monthlyDataXAxis)
        val barData = BarData(barDataSet)
        viewBinding.contentMainContainer.barChart.setFitBars(true)
        viewBinding.contentMainContainer.barChart.setDragEnabled(true)
        viewBinding.contentMainContainer.barChart.setData(barData)
        barData.barWidth = 0.5f
        viewBinding.contentMainContainer.barChart.setExtraOffsets(5f,5f,5f,15f)
        //Y-axis
        viewBinding.contentMainContainer.barChart.getAxisRight().setEnabled(false)
        val leftAxis: YAxis = viewBinding.contentMainContainer.barChart.getAxisLeft()
        leftAxis.setDrawGridLines(true)
//        leftAxisro.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f)
        val yAxisRight: YAxis = viewBinding.contentMainContainer.barChart.getAxisRight()
        yAxisRight.setEnabled(false)
        viewBinding.contentMainContainer.barChart.setExtraBottomOffset(1f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMaximum(12f)
//        viewBinding.contentMainContainer.barChart.setVisibleXRangeMinimum(12f)
        viewBinding.contentMainContainer.barChart.invalidate()

    }


//    class IntValueFormatter : IValueFormatter {
//        fun getFormattedValue(
//            value: Float,
//            entry: Entry?,
//            dataSetIndex: Int,
//            viewPortHandler: ViewPortHandler?
//        ): String {
//            return value as Int.toString()
//        }
//    }
//    class MyValueFormatter : IValueFormatter {
//        private val mFormat: DecimalFormat
//        override fun getFormattedValue(
//            value: Float,
//            entry: Entry?,
//            dataSetIndex: Int,
//            viewPortHandler: ViewPortHandler?
//        ): String {
//            // write your logic here
//            return mFormat.format(value).toString() + " $" // e.g. append a dollar-sign
//        }
//
//        init {
//            mFormat = DecimalFormat("###,###,##0.0") // use one decimal
//        }
//    }
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
            gravity = Gravity.CENTER
        }

    }

    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems = regionalOffice.distinctBy { it.name }
        regionalOffice.clear()
        regionalOffice.addAll(setItems)
        afterResetRO.addAll(regionalOffice)
        val itemChecked = BooleanArray(regionalOffice.size)
        var dayList = java.util.ArrayList<Int>()

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
                roselectedId = resultList.joinToString(separator = ",") { "${it.id}" }

//                selectionCompleteListener?.onCompleteSelection(resultList)
            }
//            alertDialog.setNegativeButton("clear"){ DialogInterface, i: Int ->
//                val resultList =ArrayList<RegionalOfficeFilterData>()
//                for (i in 0 until regionalOffice.size) {
//                    if (regionalOffice[i].isSelected) {
//                        regionalOffice[i].isSelected = false
//                        resultList.remove(regionalOffice[i])
//                    }
//                }
//                regionalspinner.text = ""
//                selectionCompleteListener?.onCompleteSelection(resultList)
//            }

            alertDialog.show()
        }

//        regionalspinner.setOnClickListener{
//            SearchableMultiSelectSpinner.show(this, "Select Regional Office", "Done", regionalOffice, object :
//                SelectionCompleteListener {
//                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
////                    Log.e("data", selectedItems.toString())
//                    val selectedItemList = selectedItems.toString().substring(1, selectedItems.toString().length - 1)
//                    roselectedId= selectedItems.joinToString (separator = ","){ "${it.id}" }
//                    Log.d("nja", roselectedId)
//                    regionalspinner.text = selectedItemList
//                }
//            })
//        }
        resestTV.setOnClickListener {
//            regionalspinner.text = ""//TODO
//            roselectedId = ""
            val resultList = ArrayList<RegionalOfficeFilterData>()
            val stringBuilder = StringBuilder()
            for (i in 0 until regionalOffice.size) {
                if (regionalOffice[i].isSelected) {
                    regionalOffice[i].isSelected = false
                    resultList.remove(regionalOffice[i])
                    regionalspinner.setText("")
                }
            }

            subadminSpinner.setSelection(0)
        }


//            val dayArray = listOf<String>("monday","tuesday","wednesday","thrusday","friday")
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Select Day")
//            builder.setCancelable(false)
//            val list = Arrays.asList(regionalOffice)
//            builder.setMultiChoiceItems(list, itemChecked) { dialog, i, isChecked ->
//                // Update the current focused item's checked status
//                if (isChecked){
//                    dayList.add(i)
//                    Collections.sort(dayList)
//                }else{
//                    dayList.remove(i)
//                }
//
//            }
//            builder.setPositiveButton("ok") { dialog, which ->
//                val stringBuilder = StringBuilder()
//                for (i in dayList.indices) {
//                    stringBuilder.append(regionalOffice.get(dayList[i]))
//                    if (i != dayList.size - 1) {
//                        stringBuilder.append(",")
//                    }
//                }
//                regionalspinner.text= stringBuilder.toString()
//            }
//            builder.show()

    }

    private fun setFinancialYearTypeSpinner(financialdata: MutableList<FinancialYearData>) {
        financialdata.sortBy { it.name }

        for (i in 0 until financialdata.size) {

            var startDate = financialdata.get(i).startdate
            val endDate = financialdata.get(i).enddate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

//          val stDate =DateTimeHelper.getDDMMYYYYDateFromString(startdate)
//            val enDate = DateTimeHelper.getDDMMYYYYDateFromString(endDate)
            val stDate = dateFormat.parse(startDate)
            val enDate = dateFormat.parse(endDate)
            val currentdate = Calendar.getInstance().time
//            val currentdate = dateFormat.format(cdate)


            Log.d("sjh", enDate.toString())


            if (currentdate.after(stDate) && currentdate.before(enDate)) {

                fyearDateName = financialdata.get(i).name
                fyearDateId = financialdata.get(i).id
                startdate = financialdata.get(i).startdate
                enddate = financialdata.get(i).enddate

            } else {
//               Toast.makeText(this,"nxjj",Toast.LENGTH_LONG).show()
            }


        }

        startDateAndEndDatePassForDashBoard()

        financialdata.add(
                0,
                FinancialYearData(
                        id = fyearDateId!!,
                        name = fyearDateName!!,
                        startdate = startdate!!,
                        enddate = enddate!!
                )
        )

        val setItems = financialdata.distinctBy { it.name }
        financialdata.clear()
        financialdata.addAll(setItems)

        val aa = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                financialdata
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(financialspinner) {
            adapter = aa
            prompt = "Select Financial Year"
            gravity = android.view.Gravity.CENTER
        }


    }

    private fun startDateAndEndDatePassForDashBoard() {

        defaultStartDate = startdate.toString()
        defaultEndDate = enddate.toString()
        viewModel.dashBoardData(defaultStartDate, defaultEndDate, regionalOfficeIds,subadminId)
    }
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            selectedTab = tab?.text.toString()
            tabPosition = tab?.position

//            defaultStartDate = startdate.toString()
//            defaultEndDate = enddate.toString()
//            viewModel.dashBoardData(defaultStartDate, defaultEndDate, regionalOfficeIds,subadminId)

            if (selectedTab!!.toLowerCase().equals("summary")) {
                viewBinding.contentMainContainer.summaryContainer.setVisibilityVisible()
                viewBinding.contentMainContainer.monthlyContainer.setVisibilityGone()
                viewBinding.contentMainContainer.roWiseContainer.setVisibilityGone()

            } else if (selectedTab!!.toLowerCase().equals("monthly")) {
                viewBinding.contentMainContainer.monthlyContainer.setVisibilityVisible()
                viewBinding.contentMainContainer.summaryContainer.setVisibilityGone()
                viewBinding.contentMainContainer.roWiseContainer.setVisibilityGone()
            }else if (selectedTab!!.toLowerCase().equals("ro wise")){
                viewBinding.contentMainContainer.summaryContainer.setVisibilityGone()
                viewBinding.contentMainContainer.roWiseContainer.setVisibilityVisible()
                viewBinding.contentMainContainer.monthlyContainer.setVisibilityGone()

            }

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }
    }


    private fun initBottomsheetFileterView() {


        applyBtn.setOnClickListener {
            var sdate: String? = null
            var eDate: String? = null
            financialspinner.onItemSelected { adapterView, _, _, _ ->
                adapterView ?: return@onItemSelected
                val financialData = financialspinner.selectedItem as FinancialYearData
//                startdatef = financialData.startdate
//                enddatef  = financialData.enddate
            }
            val activeSubadminId = (subadminSpinner.selectedItem as ActiveSubAdminData).id.toString()
                val financialData = financialspinner.selectedItem as FinancialYearData
                startdatef = financialData.startdate
                enddatef = financialData.enddate
                startDateF = startdatef.toString()
                endDateF = enddatef.toString()
                val roIds = roselectedId
                viewModel.dashBoardData(startDateF, endDateF, roselectedId,subadminId)
                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN)
                bottomSheetDialog.dismiss()

            }

        }





        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_complaint -> {
                    val intent = Intent(this, ComplaintsActivity::class.java)
                    startActivity(intent)

                }
//                R.id.nav_billing -> {
//                    Toast.makeText(this, "Comming soon", Toast.LENGTH_LONG).show()
//                }
                R.id.nav_doc -> {
                    val intent = Intent(this, DocumentsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_attendance -> {
                    val intent = Intent(this, AttendanceActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_expanse -> {
                    val intent = Intent(this, ExpenseActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_tutorial -> {
                    val intent = Intent(this, TutorialsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_outlet -> {
                    val intent = Intent(this, OutletsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_logout -> {
                    MaterialAlertDialogBuilder(this)
                            .setTitle("Log Out")
                            .setMessage("Do you want to log out?")
                            .setPositiveButton("Yes") { _, _ ->
//                        userSessionManager.logOut()
                                viewModel.logout()
                            }.setNegativeButton("No") { dialog, _ ->
                                dialog.cancel()
                            }.show()
                }
            }
            return true
        }

        override fun onItemClick(position: Int) {
//        when(position){
//            0 ->
//            {
////                val intent = Intent(this, ComplaintsActivity::class.java)
////                startActivity(intent)
//            }
//            1->
//            {
////                val intent = Intent(this, DocumentsActivity::class.java)
////                startActivity(intent)
//            }
//            2->{
////                val intent = Intent(this, AttendanceActivity::class.java)
////                startActivity(intent)
//            }
//            3->{
////                val intent = Intent(this, ExpenseActivity::class.java)
////                startActivity(intent)
//            }
//            4->{
////                val intent = Intent(this, TutorialsActivity::class.java)
////                startActivity(intent)
//            }
//
//
//        }
        }


    }




