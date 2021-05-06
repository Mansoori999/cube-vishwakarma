package com.vinners.cube_vishwakarma.ui.complaints.myComplaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMainBinding
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import javax.inject.Inject

class MyComplaintActivity: BaseActivity<ActivityMyComplaintBinding,MyComplaintViewModel>(R.layout.activity_my_complaint) {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private val sectionsPagerAdapter: MyComplaintPagerAdapter by lazy {
        MyComplaintPagerAdapter(supportFragmentManager)

    }

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: MyComplaintViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        setSupportActionBar(viewBinding.mycomplaintToolbar)
        getSupportActionBar()!!.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_left))
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        viewBinding.mycomplaintToolbar.setNavigationOnClickListener { view -> onBackPressed() }

        tabs = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.view_pager)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
    override fun onInitViewModel() {

    }
}