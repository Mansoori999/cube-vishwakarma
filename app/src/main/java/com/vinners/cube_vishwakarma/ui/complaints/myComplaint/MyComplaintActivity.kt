package com.vinners.cube_vishwakarma.ui.complaints.myComplaint

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMainBinding
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*
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
        getSupportActionBar()!!.setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_arrow_left,null))
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.complaint_search_menu, menu)
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
                if (viewPager.currentItem  ==0)
                {
                    val fragment =  viewPager.adapter!!.instantiateItem(viewPager, 0) as Fragment
                    if ( fragment is AllFragment)
                        fragment.allComplaintSearchFilter(newText)
                } else if (viewPager.currentItem  == 1) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 1) as Fragment
                    if (fragment is DueFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 2) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 2) as Fragment
                    if (fragment is WorkingFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 3) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 3) as Fragment
                    if (fragment is HoldFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 4) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 4) as Fragment
                    if (fragment is CancelledFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 5) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 5) as Fragment
                    if (fragment is DoneFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 6) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 6) as Fragment
                    if (fragment is DraftFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 7) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 7) as Fragment
                    if (fragment is EstimatedFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 8) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 8) as Fragment
                    if (fragment is BilledFragment)
                        fragment.allComplaintSearchFilter(newText)
                }else if (viewPager.currentItem  == 9) {
                    val fragment = viewPager.adapter!!.instantiateItem(viewPager, 9) as Fragment
                    if (fragment is PaymentFragment)
                        fragment.allComplaintSearchFilter(newText)
                }

                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}