package com.vinners.cube_vishwakarma.ui.tutorials

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.databinding.ActivityEditOutletBinding
import com.vinners.cube_vishwakarma.databinding.ActivityTutorialsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.outlets.OutletRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.outlets.OutletsViewModel
import javax.inject.Inject

class TutorialsActivity : BaseActivity<ActivityTutorialsBinding, TutorialsViewModel>(R.layout.activity_tutorials) {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory


    override val viewModel: TutorialsViewModel by viewModels { viewModelFactory }

    private val tutorialRecyclerAdapter: TutorialsRecyclerAdapter by lazy {
        TutorialsRecyclerAdapter()
                .apply {
                    updateViewList(emptyList())

                }
    }

    override fun onInitDataBinding() {
        viewBinding.tutorialToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.tutorialRecycler.layoutManager = LinearLayoutManager(this)
        tutorialRecyclerAdapter.updateViewList(emptyList())
        viewBinding.tutorialRecycler.adapter = tutorialRecyclerAdapter
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            viewModel.getTutorialsData()

        }


    }

    override fun onInitViewModel() {
        viewModel.tutorialsState.observe(this, Observer {
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
                        viewBinding.errorLayout.messageTv.text = "Not Tutorials Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        tutorialRecyclerAdapter.updateViewList(it.content)
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

        viewModel.getTutorialsData()

    }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
                .builder()
                .coreComponent(getCoreComponent())
                .build()
                .inject(this)
    }
}