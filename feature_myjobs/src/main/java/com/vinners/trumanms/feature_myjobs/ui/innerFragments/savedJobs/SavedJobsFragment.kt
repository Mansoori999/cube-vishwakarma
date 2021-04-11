package com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobs.SavedJob
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentSavedJobsBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import javax.inject.Inject

class SavedJobsFragment :
    BaseFragment<FragmentSavedJobsBinding, SavedJobsViewModel>(R.layout.fragment_saved_jobs),
    SavedJobRecyclerAdapterListener {

    companion object {
        const val TAG = "SavedJobsFragment"
    }

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    private val savedJobsRecyclerViewAdapter: SavedJobsJobRecyclerAdapter by lazy {
        SavedJobsJobRecyclerAdapter(appInfo).apply {
            setOnClickListener(this@SavedJobsFragment)
        }
    }

    override val viewModel: SavedJobsViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.savedJobsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.savedJobsRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.savedJobsRecyclerView.adapter = savedJobsRecyclerViewAdapter
        viewBinding.saveJobRefresh.setOnRefreshListener {
            viewModel.getSavedJob()
            viewBinding.saveJobRefresh.isRefreshing = false
        }
    }

    override fun onInitViewModel() {
        viewModel
            .viewState
            .observe(viewLifecycleOwner, Observer {

                when (it) {
                    SavedJobsViewModelStates.LoadingJobs -> jobsLoading()
                    is SavedJobsViewModelStates.SavedJobsLoaded -> jobsLoaded(it.jobs)
                    is SavedJobsViewModelStates.ErrorWhileFetchingJobs -> errorWhileLoadingJobs(it.error)

                    SavedJobsViewModelStates.ApplyingJob -> {
                    }
                    is SavedJobsViewModelStates.AppliedForJob -> {
                        Toast.makeText(requireContext(), "Applied For Job", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is SavedJobsViewModelStates.ErrorWhileApplyingJob -> {

                    }
                }
            })

        viewModel.getSavedJob()
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        savedJobsRecyclerViewAdapter.updateList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(jobs: List<SavedJob>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (jobs.isEmpty()) {

            savedJobsRecyclerViewAdapter.updateList(jobs)
            showNoJobsFoundLayout()
        } else {

            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            savedJobsRecyclerViewAdapter.updateList(jobs)
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
        savedJobsRecyclerViewAdapter.updateList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.getSavedJob()
        }
    }

    override fun shareJobClicked(position: Int, savedJob: SavedJob) {
        Toast.makeText(requireContext(), "Will Implement Soon", Toast.LENGTH_SHORT).show()
    }

    override fun applyJobClicked(position: Int, savedJob: SavedJob) {

        if (savedJob.hasAppliedForThisJob) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Alert")
                .setMessage("You have already applied for this job")
                .setPositiveButton("OK") { _, _ ->
                }
                .show()

        } else {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Alert")
                .setMessage("Are you sure you want to apply for this Job")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.applyForJob(savedJob.jobId)
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JobDetailsActivity.REQUEST_CODE)
            viewModel.getSavedJob()
    }

    override fun onJobClick(savedJob: SavedJob) {
        val intent = Intent(requireContext(),JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.IS_SAVE_FRAGMENT,true)
        intent.putExtra(JobDetailsActivity.JOB_ID,savedJob.jobId)
        startActivityForResult(intent,JobDetailsActivity.REQUEST_CODE)
    }
}