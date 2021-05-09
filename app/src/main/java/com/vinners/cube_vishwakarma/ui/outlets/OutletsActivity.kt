package com.vinners.cube_vishwakarma.ui.outlets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import androidx.lifecycle.Observer
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.ActivityOutletsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*
import java.util.*
import javax.inject.Inject

class OutletsActivity : BaseActivity<ActivityOutletsBinding,OutletsViewModel>(R.layout.activity_outlets) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override val viewModel: OutletsViewModel by viewModels { viewModelFactory }

    private val outletRecyclerAdapter: OutletRecyclerAdapter by lazy {
        OutletRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())
                    setOutletListener(this@OutletsActivity)
                }
    }

    private fun setOutletListener(outletsClickListener: OutletsActivity) {

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

            viewModel.getOutletData()

        }

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
                outletRecyclerAdapter.getFilter().filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}