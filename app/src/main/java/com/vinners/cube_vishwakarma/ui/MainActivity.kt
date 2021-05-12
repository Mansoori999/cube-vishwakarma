package com.vinners.cube_vishwakarma.ui


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.vinners.cube_vishwakarma.BuildConfig
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.homeScreen.MainActivityListModel
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMainBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.feature_auth.ui.AuthActivity
import com.vinners.cube_vishwakarma.feature_auth.ui.login.LoginFragment
import com.vinners.cube_vishwakarma.ui.attendance.AttendanceActivity

import com.vinners.cube_vishwakarma.ui.complaints.ComplaintsActivity
import com.vinners.cube_vishwakarma.ui.documents.DocumentsActivity
import com.vinners.cube_vishwakarma.ui.expense.ExpenseActivity
import com.vinners.cube_vishwakarma.ui.outlets.OutletsActivity
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsActivity
import de.hdodenhof.circleimageview.CircleImageView

import java.io.File
import java.lang.String
import javax.inject.Inject


class MainActivity : BaseActivity<ActivityMainBinding , MainActivityViewModel>(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener,MainActivityRecyclerAdapter.ClickListener{
    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

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
        navigationView.setNavigationItemSelectedListener(this)
        val inflater: LayoutInflater = this@MainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewGroup : ViewGroup = findViewById (R.id.nav_view)
        var view = inflater.inflate(R.layout.nav_header_main, viewGroup)
        val userName = view.findViewById<TextView>(R.id.username)
        userName.setText(String.format("Hii, %s", userSessionManager.userName))
        val userMobile = view.findViewById<TextView>(R.id.userMobile)
        userMobile.text = userSessionManager.mobile
        val appVersionTV = view.findViewById<TextView>(R.id.appVersionTV)
        appVersionTV.text = "App Version: ${BuildConfig.VERSION_NAME}"

        val complaints = view.findViewById<CardView>(R.id.complaint_container)
        val documents = view.findViewById<CardView>(R.id.doc_container)
        val attendance = view.findViewById<CardView>(R.id.attendance_container)
        val expense = view.findViewById<CardView>(R.id.expense_container)
        val tutorials = view.findViewById<CardView>(R.id.tutorial_container)
        val outlets = view.findViewById<CardView>(R.id.outlets_container)
       val  myAnim : Animation = AnimationUtils.loadAnimation(this, R.anim.card_anim);

        complaints.setOnClickListener {
            complaints.startAnimation(myAnim);
            val intent = Intent(this, ComplaintsActivity::class.java)
            startActivity(intent)
        }
        documents.setOnClickListener {
            documents.startAnimation(myAnim);
            val intent = Intent(this, DocumentsActivity::class.java)
            startActivity(intent)
        }
        attendance.setOnClickListener {
            attendance.startAnimation(myAnim);
            val intent = Intent(this, AttendanceActivity::class.java)
            startActivity(intent)
        }
        expense.setOnClickListener {
            expense.startAnimation(myAnim);
            val intent = Intent(this, ExpenseActivity::class.java)
            startActivity(intent)
        }
        tutorials.setOnClickListener {
            tutorials.startAnimation(myAnim);
            val intent = Intent(this, TutorialsActivity::class.java)
            startActivity(intent)
        }
        outlets.setOnClickListener {
            outlets.startAnimation(myAnim);
            val intent = Intent(this, OutletsActivity::class.java)
            startActivity(intent)
        }
        val logoutBtn = findViewById<ImageView>(R.id.logout)
        logoutBtn.setOnClickListener {
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
        val recyclerView: RecyclerView = findViewById(R.id.Recyclerview_home)
        mainActivityRecyclerAdapter.updateViewList(homeList)
        recyclerView.adapter = mainActivityRecyclerAdapter
        preparehomeData()

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
        homeList.add(MainActivityListModel("Complaints"))
        homeList.add(MainActivityListModel("Documents"))
        homeList.add(MainActivityListModel("Attendance"))
        homeList.add(MainActivityListModel("Expense"))
        homeList.add(MainActivityListModel("Tutorials"))
        homeList.add(MainActivityListModel("Outlets"))

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
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onItemClick(position: Int) {
        when(position){
            0 ->
            {
                val intent = Intent(this, ComplaintsActivity::class.java)
                startActivity(intent)
            }
            1->
            {
                val intent = Intent(this, DocumentsActivity::class.java)
                startActivity(intent)
            }
            2->{
                val intent = Intent(this, AttendanceActivity::class.java)
                startActivity(intent)
            }
            3->{
                val intent = Intent(this, ExpenseActivity::class.java)
                startActivity(intent)
            }
            4->{
                val intent = Intent(this, TutorialsActivity::class.java)
                startActivity(intent)
            }
            5->{
                val intent = Intent(this, OutletsActivity::class.java)
                startActivity(intent)
            }

        }
    }


}