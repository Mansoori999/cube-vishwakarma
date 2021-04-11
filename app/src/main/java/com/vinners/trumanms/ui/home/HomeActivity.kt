package com.vinners.trumanms.ui.home

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import coil.api.load
import com.example.feature_profile.ui.ProfileActivity
import com.example.notification.ui.NotificationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vinners.trumanms.R
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.databinding.ActivityHomeBinding
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.di.LauncherViewModelFactory
import com.vinners.trumanms.feature.wallet.ui.ui.WalletFragment
import com.vinners.trumanms.feature_help.ui.HelpFragment
import com.vinners.trumanms.feature_job.ui.jobList.jobList.JobListFragment
import com.vinners.trumanms.feature_myjobs.ui.myJobs.MyJobsFragment
import com.vinners.trumanms.notification.NotificationDataKeyValue
import java.io.File
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {
    companion object {
        const val INTENT_EXTRA_NOTIFICATION_APPLIED = "notify_applied"
        const val INTENT_EXTRA_NOTIFICATION_TYPE = "type"
    }

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: HomeViewModel by viewModels {
        viewModelFactory
    }

    private var isBackPresedOnce: Boolean = false
    private var notificationType: String? = null

    private val homeViewPagerAdapter: HomeViewPagerAdapter by lazy {
        HomeViewPagerAdapter(supportFragmentManager)
    }

    private val bottonNavigateListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_jobs -> {
                        val fragmentTranction = supportFragmentManager.beginTransaction()
                        fragmentTranction.replace(
                            viewBinding.fragmentContainer.id,
                            JobListFragment()
                        ).commit()

                    }
                    R.id.navigation_myJobs -> {
                        val fragmentTranction = supportFragmentManager.beginTransaction()
                        fragmentTranction.replace(
                            viewBinding.fragmentContainer.id,
                            MyJobsFragment()
                        ).commit()

                    }
                    R.id.navigation_money -> {
                        val fragmentTranction = supportFragmentManager.beginTransaction()
                        fragmentTranction.replace(
                            viewBinding.fragmentContainer.id,
                            WalletFragment()
                        ).commit()
                    }
                    R.id.navigation_help -> {
                        val fragmentTranction = supportFragmentManager.beginTransaction()
                        fragmentTranction.replace(
                            viewBinding.fragmentContainer.id,
                            HelpFragment()
                        ).commit()
                    }
                }
                return true
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
        // initViewPager()
        notificationType = intent.getStringExtra(INTENT_EXTRA_NOTIFICATION_TYPE)
        if (notificationType.equals(NotificationDataKeyValue.KEY_MY_OFFERS,true) || notificationType.equals(NotificationDataKeyValue.KEY_OPEN_TASK_IN_HAND,true)) {
            viewBinding.homeBotttomNavigation.selectedItemId = R.id.navigation_myJobs
            val myJobFragment = MyJobsFragment()
            val bundle = bundleOf(MyJobsFragment.INTENT_EXTRA_NOTIFICATION_TYPE to notificationType)
            myJobFragment.arguments = bundle
            val fragmentTranction = supportFragmentManager.beginTransaction()
            fragmentTranction.replace(viewBinding.fragmentContainer.id, myJobFragment).commit()
        }else{
            val fragmentTranction = supportFragmentManager.beginTransaction()
            fragmentTranction.replace(viewBinding.fragmentContainer.id, JobListFragment()).commit()
        }
        viewBinding.homeBotttomNavigation.itemIconTintList = null
        viewBinding.homeBotttomNavigation.setOnNavigationItemSelectedListener(bottonNavigateListener)
        viewBinding.homeHeader.notificationIcon.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        viewBinding.homeHeader.userProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (!isBackPresedOnce) {
            Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT).show()
            isBackPresedOnce = true
        } else {
            isBackPresedOnce = false
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun initViewPager() {

        val jobsFragment = JobListFragment()
        homeViewPagerAdapter.addFragment(jobsFragment)

        val myJobsFragment = MyJobsFragment()
        homeViewPagerAdapter.addFragment(myJobsFragment)

//        val myJobsFragment = MyJobsFragment()
//        homeViewPagerAdapter.addFragment(myJobsFragment)
        viewBinding.homeViewPager.adapter = homeViewPagerAdapter
        viewBinding.homeViewPager.beginFakeDrag()
    }

    override fun onInitViewModel() {
        viewModel.profile.observe(this, Observer {
            if (it.profilePic.isNullOrEmpty().not() && it.profilePicUpdated)
                viewBinding.homeHeader.userProfile.load(File(it.profilePic))
           else if (it.profilePic.isNullOrEmpty().not())
                viewBinding.homeHeader.userProfile.load(appInfo.getFullAttachmentUrl(it.profilePic!!))
           else if (it.gender.equals("Female", true))
                viewBinding.homeHeader.userProfile.load(R.drawable.avatar_woman)
            else
                viewBinding.homeHeader.userProfile.load(R.drawable.avatar_male)
        })
        viewModel.initViewModel()
    }
}