package com.vinners.cube_vishwakarma.ui.complaints.myComplaint

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityMyComplaintBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*

import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.MyComplaintSharedViewModel
import javax.inject.Inject

class MyComplaintActivity: BaseActivity<ActivityMyComplaintBinding,AllComplaintFragmentViewModel>(R.layout.activity_my_complaint), MenuItem.OnMenuItemClickListener {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    private val sectionsPagerAdapter: MyComplaintPagerAdapter by lazy {
        MyComplaintPagerAdapter(supportFragmentManager)

    }

    var userid : String? = null
    var adminUserid : String = ""

    val sharedViewModel: MyComplaintSharedViewModel by viewModels{ viewModelFactory }
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
        tabs = findViewById(R.id.tabs)
        // val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)
        viewPager = findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        dotsIndicator.setViewPager(viewPager)
//        setUpViewPager()
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
                        sharedViewModel.setcomplaints(it.content)
                        tabs.setVisibilityVisible()
                        viewPager.setVisibilityVisible()
                        viewPager.adapter = sectionsPagerAdapter
                        tabs.setupWithViewPager(viewPager)

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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (userSessionManager.designation!!.toLowerCase().equals("admin")){
                    viewModel.getComplaintList(adminUserid)
                }else{
                    viewModel.getComplaintList(userid!!)

                }
        return true
    }

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