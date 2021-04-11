package com.vinners.trumanms.feature_help.ui

import android.app.Dialog
import android.view.Window
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.jsibbold.zoomage.ZoomageView
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.certificate.Certificate
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.feature_help.R
import com.vinners.trumanms.feature_help.databinding.FragmentQueryLayoutBinding
import com.vinners.trumanms.feature_help.di.DaggerHelpComponent
import com.vinners.trumanms.feature_help.di.HelpModuleFactory
import javax.inject.Inject

class QueryFragment :
    BaseFragment<FragmentQueryLayoutBinding, HelpViewModel>(R.layout.fragment_query_layout),
    QueryClickListener {
    @Inject
    lateinit var viewModelFactory: HelpModuleFactory

    @Inject
    lateinit var appInfo: AppInfo

    override val viewModel: HelpViewModel by viewModels {
        viewModelFactory
    }

    private val queryRecyclerAdapter: QueryRecyclerAdapter by lazy {
        QueryRecyclerAdapter(appInfo).apply {
            updateLsit(emptyList())
            setListener(this@QueryFragment)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerHelpComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.queryRecycler.itemAnimator = DefaultItemAnimator()
        viewBinding.queryRecycler.adapter = queryRecyclerAdapter
        viewBinding.querySwipeRefreshLayout.setOnRefreshListener {
            viewModel.getQuery()
            viewBinding.querySwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onInitViewModel() {
        viewModel.queryState.observe(this, Observer {
            when (it) {
                Lce.Loading -> documentsLoading()
                is Lce.Content -> documentsLoaded(it.content)
                is Lce.Error -> documentsLoadFailed(it.error)
            }
        })
        viewModel.getQuery()
    }

    private fun documentsLoading() {
        viewBinding.errorLayout.root.setVisibilityGone()
        queryRecyclerAdapter.updateLsit(emptyList())
        viewBinding.progressBar.setVisibilityVisible()
    }

    private fun documentsLoaded(documentsList: List<Query>) {
        viewBinding.progressBar.setVisibilityGone()
        if (documentsList.isEmpty()) {
            queryRecyclerAdapter.updateLsit(emptyList())
            showNoDataFoundLayout()
        } else {
            viewBinding.errorLayout.root.setVisibilityGone()
            queryRecyclerAdapter.updateLsit(documentsList)
        }
    }

    private fun showNoDataFoundLayout() {
        viewBinding.errorLayout.root.setVisibilityVisible()
        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = "No Queries Found"
        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
    }

    private fun documentsLoadFailed(errorMessage: String) {
        viewBinding.progressBar.setVisibilityGone()
        queryRecyclerAdapter.updateLsit(emptyList())
        viewBinding.errorLayout.root.setVisibilityVisible()

        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.errorLayout.messageTv.text = errorMessage
        viewBinding.errorLayout.errorActionButton.setVisibilityVisible()
        viewBinding.errorLayout.errorActionButton.text = "Re-Try"
        viewBinding.errorLayout.errorActionButton.setOnClickListener {
            viewModel.getQuery()
        }
    }

    override fun onQueryClick(query: Query) {
        val dialog =
            Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(R.layout.big_picture_image)
                // window.attributes.windowAnimations = R.style.MyAlertDialogStyle
            }
        val fullPic = dialog.findViewById<ZoomageView>(R.id.big_picture)
        if (query.path.isNullOrEmpty().not())
            fullPic.load(appInfo.getFullAttachmentUrl(query.path!!))
        dialog.show()
    }

     fun refreshList() {
        viewModel.getQuery()
    }
}