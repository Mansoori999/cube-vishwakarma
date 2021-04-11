package com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobs.AppliedJob
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentAppliedJobsBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw.WithdrawApplicationDialogFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw.WithdrawApplicationDialogFragmentEventListener
import javax.inject.Inject


class AppliedJobsFragment :
    BaseFragment<FragmentAppliedJobsBinding, AppliedJobsViewModel>(R.layout.fragment_applied_jobs),
    AppliedJobsJobRecyclerAdapterListener, WithdrawApplicationDialogFragmentEventListener {

    companion object{
        const val TAG = "AppliedJobsFragment"
    }

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo


    private val appliedJobsAdapter: AppliedJobsJobRecyclerAdapter by lazy {
        AppliedJobsJobRecyclerAdapter(appInfo).apply {
            setOnClickListener(this@AppliedJobsFragment)
        }
    }

    override val viewModel: AppliedJobsViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.appliedJobsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.appliedJobsRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.appliedJobsRecyclerView.adapter = appliedJobsAdapter
        viewBinding.appliedJobRefresh.setOnRefreshListener {
            viewModel.fetchAppliedJobs()
            viewBinding.appliedJobRefresh.isRefreshing = false
        }
    }

    override fun onInitViewModel() {

        viewModel
            .viewState
            .observe(viewLifecycleOwner, Observer {

                when (it) {
                    AppliedJobsViewModelStates.LoadingJobs -> jobsLoading()
                    is AppliedJobsViewModelStates.AppliedJobsLoaded -> jobsLoaded(it.jobs)
                    is AppliedJobsViewModelStates.ErrorWhileFetchingJobs -> errorWhileLoadingJobs(it.error)
                }
            })

        viewModel.fetchAppliedJobs()
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        appliedJobsAdapter.updateList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(jobs: List<AppliedJob>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (jobs.isEmpty()) {

            appliedJobsAdapter.updateList(jobs)
            showNoJobsFoundLayout()
        } else {

            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            appliedJobsAdapter.updateList(jobs)
        }
    }

    private fun showNoJobsFoundLayout() {
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = "No Tasks Found"
        viewBinding.jobsInfoLayout.errorActionButton.setVisibilityGone()
    }

    private fun errorWhileLoadingJobs(error: String) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        appliedJobsAdapter.updateList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.fetchAppliedJobs()
        }
    }

    override fun shareJobClicked(position: Int, appliedJob: AppliedJob) {
        Toast.makeText(requireContext(), "Implement soon", Toast.LENGTH_SHORT).show()
    }

    override fun withdrawApplication(position: Int, appliedJob: AppliedJob) {
        WithdrawApplicationDialogFragment.launch(
            jobId = appliedJob.jobId,
            applicationId = appliedJob.applicationId,
            fragmentManager = childFragmentManager,
            jobWithdrawalListener = this@AppliedJobsFragment
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JobDetailsActivity.REQUEST_CODE)
            viewModel.fetchAppliedJobs()
    }

    override fun jobApplicationWithdrawn(jobId: String) {
        viewModel.fetchAppliedJobs()
    }

    override fun onJobClick(appliedJob: AppliedJob) {
        val intent = Intent(requireContext(),JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.IS_APPLIED_FRAGMENT,true)
        intent.putExtra(JobDetailsActivity.APPLICATION_ID,appliedJob.applicationId)
        intent.putExtra(JobDetailsActivity.JOB_ID,appliedJob.jobId)
        startActivityForResult(intent,JobDetailsActivity.REQUEST_CODE)
    }
}