package com.example.feature_profile.ui.jobHistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.feature_profile.R
import com.example.feature_profile.databinding.ActivityJobHistoryBinding
import com.example.feature_profile.di.DaggerProfileComponent
import com.example.feature_profile.di.ProfileViewModelFactory
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.EndlessRecyclerViewOnScrollListener
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.jobHistory.JobHistory
import com.vinners.trumanms.data.models.jobs.Job
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import javax.inject.Inject

class JobHistoryActivity : BaseActivity<ActivityJobHistoryBinding,JobHistoryViewModel>(R.layout.activity_job_history),JobInteractionListener {

    @Inject
    lateinit var viewModelFactory: ProfileViewModelFactory
    override val viewModel: JobHistoryViewModel by viewModels {
        viewModelFactory
    }
    @Inject
    lateinit var appInfo: AppInfo

    private val jobHistoryViewRecyclerAdapter: JobHistoryViewRecyclerAdapter by lazy {
        JobHistoryViewRecyclerAdapter(appInfo).apply {
            setListInteractionListener(this@JobHistoryActivity)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerProfileComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.jobHistoryRecyclerView.itemAnimator =  DefaultItemAnimator()
        viewBinding.jobHistoryRecyclerView.setHasFixedSize(true)
        viewBinding.jobHistoryRecyclerView.isMotionEventSplittingEnabled = false
        val linearLayoutManager = LinearLayoutManager(this)
        viewBinding.jobHistoryRecyclerView.layoutManager = linearLayoutManager
        viewBinding.jobHistoryRecyclerView.adapter = jobHistoryViewRecyclerAdapter
        viewBinding.jobHistoryRecyclerView.addOnScrollListener(setupScrollListener(linearLayoutManager))
        viewBinding.refreshJobHistoryLayout.setOnRefreshListener {
            jobHistoryViewRecyclerAdapter.removeAll()
            viewModel.getJobHistory(0)
            viewBinding.refreshJobHistoryLayout.isRefreshing = false
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setupScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : EndlessRecyclerViewOnScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getJobHistory(page)
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.jobHistoryState.observe(this, Observer {
            when(it){
                Lce.Loading -> {
                    viewBinding.errorLayout.root.setVisibilityGone()
                    if (jobHistoryViewRecyclerAdapter.isEmpty)
                        viewBinding.progressBar.setVisibilityVisible()
                    else{
                        jobHistoryViewRecyclerAdapter.removeErrorView()
                        jobHistoryViewRecyclerAdapter.addLoadingView()
                    }
                }
               is Lce.Content -> {
                   viewBinding.progressBar.setVisibilityGone()
                    if (jobHistoryViewRecyclerAdapter.isEmpty && it.content.isEmpty()){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = " No Task Found"
                    }else{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        jobHistoryViewRecyclerAdapter.removeLoadingView()
                        jobHistoryViewRecyclerAdapter.addItems(it.content)
                    }
                }
                is Lce.Error -> {
                    viewBinding.progressBar.setVisibilityGone()
                    if (jobHistoryViewRecyclerAdapter.isEmpty){
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = it.error
                    }else{
                        viewBinding.errorLayout.root.setVisibilityGone()
                        jobHistoryViewRecyclerAdapter.removeLoadingView()
                        jobHistoryViewRecyclerAdapter.addErrorView(it.error)
                    }
                }
            }
        })
        viewModel.getJobHistory(0)
    }

    override fun onJobListItemClick(job: JobHistory) {
        val intent = Intent(this,JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.JOB_ID,job.jobId.toString())
        intent.putExtra(JobDetailsActivity.IS_FROM_JOB_HISTORY,true)
        intent.putExtra(JobDetailsActivity.JOB_HISTORY_STATUS,job.status)
        startActivity(intent)
    }

}