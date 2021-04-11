package com.example.feature_profile.ui.documents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityDocumentsBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.Documents
import java.util.*
import javax.inject.Inject

class DocumentsActivity :
    BaseActivity<ActivityDocumentsBinding, DocumentsViewModel>(R.layout.activity_documents) {
    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: DocumentsViewModel by viewModels {
        viewModelFactory
    }

    private val documentsRecyclerAdapter: DocumentsRecyclerAdapter by lazy {
        DocumentsRecyclerAdapter(appInfo).apply {
            setDocumentsList(Collections.emptyList())
        }
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.documentsRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.documentsRecyclerView.adapter = documentsRecyclerAdapter
        viewBinding.refreshDocumentsLayout.setOnRefreshListener {
            viewModel.getDocuments()
            viewBinding.refreshDocumentsLayout.isRefreshing = false
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onInitViewModel() {
        viewModel.documentsStates.observe(this, Observer {
            when (it) {
                Lce.Loading -> documentsLoading()
                is Lce.Content -> documentsLoaded(it.content)
                is Lce.Error -> documentsLoadFailed(it.error)
            }
        })
        viewModel.getDocuments()
    }

    private fun documentsLoading() {
        viewBinding.errorLayout.root.setVisibilityGone()
        documentsRecyclerAdapter.setDocumentsList(emptyList())
        viewBinding.progressBar.setVisibilityVisible()
    }

    private fun documentsLoaded(documentsList: List<Documents>) {
        viewBinding.progressBar.setVisibilityGone()
        if (documentsList.isEmpty()){
            documentsRecyclerAdapter.setDocumentsList(emptyList())
            showNoDataFoundLayout()
        }
        else{
            viewBinding.errorLayout.root.setVisibilityGone()
            documentsRecyclerAdapter.setDocumentsList(documentsList)
        }
    }

    private fun showNoDataFoundLayout() {
        viewBinding.errorLayout.root.setVisibilityVisible()
        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = "No Documents Found"
        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
    }

    private fun documentsLoadFailed(errorMessage: String) {
        viewBinding.progressBar.setVisibilityGone()
        documentsRecyclerAdapter.setDocumentsList(emptyList())
        viewBinding.errorLayout.root.setVisibilityVisible()

        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = errorMessage
        viewBinding.errorLayout.errorActionButton.setVisibilityVisible()
        viewBinding.errorLayout.errorActionButton.text = "Re-Try"
        viewBinding.errorLayout.errorActionButton.setOnClickListener {
            viewModel.getDocuments()
        }
    }

}