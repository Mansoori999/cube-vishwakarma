package com.vinners.cube_vishwakarma.ui


import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import coil.api.load
import com.androidbuts.multispinnerfilter.KeyPairBoolData
import com.androidbuts.multispinnerfilter.MultiSpinnerListener
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch
import com.devstune.searchablemultiselectspinner.SearchableMultiSelectSpinner
import com.devstune.searchablemultiselectspinner.SelectionCompleteListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.vinners.cube_vishwakarma.BuildConfig
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
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
import com.vinners.cube_vishwakarma.ui.dashboardFilter.RegionalOfficeFilterData
import com.vinners.cube_vishwakarma.ui.documents.DocumentsActivity
import com.vinners.cube_vishwakarma.ui.expense.ExpenseActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletComplaintsActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletsActivity
import com.vinners.cube_vishwakarma.ui.profile.ProfileActivity
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.String
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding , MainActivityViewModel>(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener,MainActivityRecyclerAdapter.ClickListener{
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    private lateinit var bottomSheetView:View
    private lateinit var regionalspinner:TextView


    @Inject
    lateinit var appInfo : AppInfo

    private val homeList = ArrayList<MainActivityListModel>()

    private val mainActivityRecyclerAdapter: MainActivityRecyclerAdapter by lazy {
        MainActivityRecyclerAdapter(this)
            .apply {
                updateViewList(homeList = emptyList())
            }
    }



    override val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }

    override fun onInitDataBinding() {

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
        setProfilePicture()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val drawermenu : ImageView = findViewById(R.id.drawer_menu)
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
        val  myAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.card_anim)
        viewBinding.contentMainContainer.totalCardView.setOnClickListener {
            viewBinding.contentMainContainer.totalCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_TOTAL_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.dueCardView.setOnClickListener {
            viewBinding.contentMainContainer.dueCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DUE_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.workingCardView.setOnClickListener {
            viewBinding.contentMainContainer.workingCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_WORKING_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.pendingCardView.setOnClickListener {
            viewBinding.contentMainContainer.pendingCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PENDING_ACTIVITY, true)
            startActivity(intent)

        }

        viewBinding.contentMainContainer.doneCardView.setOnClickListener {
            viewBinding.contentMainContainer.doneCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DONE_ACTIVITY, true)
            startActivity(intent)

        }

        viewBinding.contentMainContainer.draftCardView.setOnClickListener {
            viewBinding.contentMainContainer.draftCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_DRAFT_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.estimatedCardView.setOnClickListener {
            viewBinding.contentMainContainer.estimatedCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_ESTIMATE_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.billedCardView.setOnClickListener {
            viewBinding.contentMainContainer.billedCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_BILLED_ACTIVITY, true)
            startActivity(intent)

        }
        viewBinding.contentMainContainer.paymentCardView.setOnClickListener {
            viewBinding.contentMainContainer.paymentCardView.startAnimation(myAnim)
            val intent = Intent(this, OutletComplaintsActivity::class.java)
            intent.putExtra(OutletComplaintsActivity.ENABLE_PAYMENT_ACTIVITY, true)
            startActivity(intent)

        }

        initBottomsheetFileterView()


    }

    private fun initBottomsheetFileterView() {
        val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, p1: Int) {}
        }
        bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_filter_layout, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        regionalspinner = bottomSheetView.findViewById(R.id.financial_spinner)
//        regionalspinner.setSearchEnabled(true)
//        regionalspinner.setClearText("Close & Clear")
//        regionalspinner.setSearchHint("Select Regioanl Office")
//        regionalspinner.setEmptyTitle("Not Data Found!")
//        for (i in regionalOffice) {
//            regionalOffice.add(RegionalOfficeFilterData(i.id, i.name, i.isSelected))
//        }
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback)
        viewBinding.contentMainContainer.filter.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.show()

        }
//        regionalspinner.onItemSelected { adapterView, _, _, _ ->
//            adapterView ?: return@onItemSelected
//
//            if (regionalspinner.childCount != 0 && regionalspinner.selectedItemPosition != 0) {
//                val regionalData = regionalspinner.selectedItem as RegionalOfficeData
//
//
//
//
//            }
//        }


    }


    private fun setProfilePicture() {
//        val profilepic = findViewById<CircleImageView>(R.id.profile_pic)
//        val profilePicUrl= userSessionManager.profilepic
//
//        if (profilePicUrl != null) {
//            val picUrl = appInfo.getFullAttachmentUrl(profilePicUrl)
//            profilepic.load(File(picUrl))
//
//       }else{
//            profilepic.setImageDrawable(getResources().getDrawable(R.drawable.user))
//        }
    }

    private fun preparehomeData() {
        homeList.add(MainActivityListModel("Totals","#99CC33"))
        homeList.add(MainActivityListModel("Due","#FF6633"))
        homeList.add(MainActivityListModel("Working","#0066CC"))
        homeList.add(MainActivityListModel("Pending Letter","#2EC43E"))
        homeList.add(MainActivityListModel("Done","#ff0000"))

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
                profilepic.setImageDrawable(getResources().getDrawable(R.drawable.user))

        })
        viewModel.initViewModel()
        viewModel.dashboardState.observe(this, Observer {
            when(it){
                Lce.Loading ->{
                    viewBinding.contentMainContainer.loadingData.setVisibilityVisible()
                }
                is Lce.Content ->{
                    viewBinding.contentMainContainer.loadingData.setVisibilityGone()
                    viewBinding.contentMainContainer.totals.text = it.content.total
                    viewBinding.contentMainContainer.due.text = it.content.due
                    viewBinding.contentMainContainer.working.text = it.content.working
                    viewBinding.contentMainContainer.pending.text = it.content.pendingletter
                    viewBinding.contentMainContainer.done.text = it.content.done
                    viewBinding.contentMainContainer.draft.text = it.content.draft
                    viewBinding.contentMainContainer.estimated.text = it.content.estimated
                    viewBinding.contentMainContainer.billed.text = it.content.billed
                    viewBinding.contentMainContainer.payment.text = it.content.payment

                }
                is Lce.Error ->{
                    viewBinding.contentMainContainer.loadingData.setVisibilityGone()
                    showInformationDialog(it.error)

                }
            }
        })
        viewModel.dashBoardData()

        viewModel.financialFilterState.observe(this, Observer {
            when(it){
                Lce.Loading->{

                }
                is Lce.Content ->{
                    val regionalOffice = it.content.map {
                        RegionalOfficeFilterData(
                            id = it.id,
                            name = it.name,
                            isSelected = false
                        )
                    }.toMutableList()
                    setRegionalOfficeTypeSpinner(regionalOffice)


                }
                is Lce.Error->{

                }
            }
        })
        viewModel.getRinancialYearFilterData()
    }

    private fun setRegionalOfficeTypeSpinner(regionalOffice: MutableList<RegionalOfficeFilterData>) {
        regionalOffice.sortBy { it.name }
        val setItems: Set<RegionalOfficeFilterData> = LinkedHashSet(regionalOffice)
        regionalOffice.clear()
        regionalOffice.addAll(setItems)
//        regionalOffice.add(
//            0,
//            RegionalOfficeFilterData(
//                id = 1,
//                name = "Select Regional Office",
//                isSelected = false
//            )
//        )

//        regionalspinner.hintText = "Select Regional Office"
        regionalspinner.setOnClickListener{
            SearchableMultiSelectSpinner.show(this, "Select Regional Office", "Done","Cancel", regionalOffice, object :
                SelectionCompleteListener {
                override fun onCompleteSelection(selectedItems: ArrayList<RegionalOfficeFilterData>) {
                    Log.e("data", selectedItems.toString())
                    regionalspinner.text = selectedItems.toString()
                }

            })

        }



//        val aa = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_dropdown_item,
//            regionalOffice
//        )
//
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        with(regionalspinner) {
//            adapter = aa
//            prompt = "Select Regional Office"
//            gravity = android.view.Gravity.CENTER
//        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_complaint ->{
                val intent = Intent(this, ComplaintsActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_doc ->{
                val intent = Intent(this, DocumentsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_attendance ->{
                val intent = Intent(this, AttendanceActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_expanse ->{
                val intent = Intent(this, ExpenseActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_tutorial ->{
                val intent = Intent(this, TutorialsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_outlet ->{
                val intent = Intent(this, OutletsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout->{
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