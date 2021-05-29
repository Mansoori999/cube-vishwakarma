package com.vinners.cube_vishwakarma.ui.documents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.BaseActivity
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.databinding.ActivityDocumentsBinding
import com.vinners.cube_vishwakarma.databinding.ActivityTutorialsBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsRecyclerAdapter
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsViewModel
import javax.inject.Inject

class DocumentsActivity : BaseActivity<ActivityDocumentsBinding, DocumentsViewModel>(R.layout.activity_documents),DocumentsClickListener {

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var appInfo : AppInfo

    override val viewModel: DocumentsViewModel by viewModels { viewModelFactory }

    private val documentRecyclerAdapter: DocumentsRecyclerAdapter by lazy {
        DocumentsRecyclerAdapter()
            .apply {
                updateViewList(emptyList(),appInfo = appInfo)
                setDocumentsListener(this@DocumentsActivity)

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
        viewBinding.documentToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        viewBinding.documentsRecycler.layoutManager = LinearLayoutManager(this)
        documentRecyclerAdapter.updateViewList(emptyList(),appInfo)
        documentRecyclerAdapter.setHasStableIds(true)
        viewBinding.documentsRecycler.adapter = documentRecyclerAdapter
        viewBinding.refreshLayout.setOnRefreshListener {

            if (!viewBinding.refreshLayout.isRefreshing) {
                viewBinding.refreshLayout.isRefreshing = true
            }

            viewModel.getDocumentsData()

        }



    }

    override fun onInitViewModel() {
        viewModel.documentState.observe(this, Observer {
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
                        viewBinding.errorLayout.messageTv.text = "Not Documents Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        viewBinding.progressBar.setVisibilityGone()
                        documentRecyclerAdapter.updateViewList(it.content.distinctBy { it.catid },appInfo)
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

        viewModel.getDocumentsData()

    }

    override fun OnDocumentsClick(documentsResponse: DocumentsResponse) {
       val intent = Intent(this,DocumentDetailsActivity::class.java)
        intent.putExtra("catid",documentsResponse.catid)
        startActivity(intent)
    }
}