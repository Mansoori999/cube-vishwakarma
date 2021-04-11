package com.vinners.trumanms.feature_job.ui.jobList.jobList

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.EndlessRecyclerViewOnScrollListener
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.jobs.Job
import com.vinners.trumanms.feature_job.R
import com.vinners.trumanms.feature_job.databinding.FragmentJobListBinding
import com.vinners.trumanms.feature_job.di.DaggerJobsComponent
import com.vinners.trumanms.feature_job.di.JobsViewModelFactory
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import java.lang.StringBuilder
import javax.inject.Inject


class JobListFragment : BaseFragment<FragmentJobListBinding, JobsViewModel>(R.layout.fragment_job_list),
    JobInteractionListener,StateFilterDialogFragment.OnStateSubmitListener {

    @Inject lateinit var viewModelFactory: JobsViewModelFactory

    @Inject lateinit var appInfo: AppInfo

    var stateItemsWrapper: String? = null
    override val viewModel: JobsViewModel by viewModels { viewModelFactory }

    private val jobRecyclerAdapter: JobsRecyclerAdapter by lazy {
        JobsRecyclerAdapter(appInfo).apply {
            setListInteractionListener(this@JobListFragment)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.jobRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.jobRecyclerView.setHasFixedSize(true)
        viewBinding.jobRecyclerView.isMotionEventSplittingEnabled = false
        viewBinding.jobRecyclerView.adapter = jobRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(requireContext())
        viewBinding.jobRecyclerView.layoutManager = linearLayoutManager
        viewBinding.jobRecyclerView.addOnScrollListener(setupScrollListener(linearLayoutManager))
        viewBinding.refreshJobList.setOnRefreshListener {
            jobRecyclerAdapter.removeAll()
            viewModel.getJobs(0,stateItemsWrapper)
            viewBinding.refreshJobList.isRefreshing = false
        }
        viewBinding.filters.setOnClickListener {
            StateFilterDialogFragment.launchDialogFragment(
                requireActivity().supportFragmentManager,
                stateItemsWrapper,
                this
            )
        }
    }

    private fun setupScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : EndlessRecyclerViewOnScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getJobs(page,stateItemsWrapper)
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.jobListState.observe(this, Observer {
            when(it){
                Lce.Loading -> {
                    viewBinding.errorLayout.root.setVisibilityGone()
                    if (jobRecyclerAdapter.isEmpty)
                        viewBinding.progressBar.setVisibilityVisible()
                    else {
                        jobRecyclerAdapter.removeErrorView()
                        jobRecyclerAdapter.addLoadingView()
                    }
                }
                is Lce.Content -> {
                    viewBinding.progressBar.setVisibilityGone()
                    if (jobRecyclerAdapter.isEmpty && it.content.isEmpty()) {
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = " No Task Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        jobRecyclerAdapter.removeLoadingView()
                        jobRecyclerAdapter.addItems(it.content)
                    }
                }
                is Lce.Error -> {
                    viewBinding.progressBar.setVisibilityGone()
                    if (jobRecyclerAdapter.isEmpty) {
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = it.error
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        jobRecyclerAdapter.removeLoadingView()
                        jobRecyclerAdapter.addErrorView(it.error)
                    }
                }
            }
        })
        viewModel.getJobs(0,stateItemsWrapper)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JobDetailsActivity.REQUEST_CODE){
            jobRecyclerAdapter.removeAll()
            viewModel.getJobs(0,stateItemsWrapper)
        }
    }

    override fun onJobListItemClick(job: Job) {
        val intent = Intent(requireContext(),JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.JOB_ID,job.id.toString())
        startActivityForResult(intent,JobDetailsActivity.REQUEST_CODE)
    }

    override fun onStatesSubmit(statesId: StringBuilder) {
        if (statesId.isEmpty())
            viewBinding.filterIcon.load(R.drawable.ic_filter_empty)
        else
            viewBinding.filterIcon.load(R.drawable.ic_filter_filled)
        stateItemsWrapper = statesId.toString()
        jobRecyclerAdapter.removeAll()
        viewModel.getJobs(0,statesId.toString())
    }
}