package com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentMyOffersBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import java.util.*
import javax.inject.Inject


class MyOffersFragment :
    BaseFragment<FragmentMyOffersBinding, MyOffersViewModel>(R.layout.fragment_my_offers),
    MyOffersOnClickListener {

    companion object {
        const val TAG = "MyOffersFragment"
    }

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    private var acceptTaskListener: AcceptTaskListener? = null

    override val viewModel: MyOffersViewModel by viewModels { viewModelFactory }

    fun setListener(acceptTaskListener: AcceptTaskListener) {
        this.acceptTaskListener = acceptTaskListener
    }

    private val myOfferJobsRecyclerAdapter: MyOffersRecyclerAdapter by lazy {
        MyOffersRecyclerAdapter(appInfo).apply {
            setMyOfferJobList(Collections.emptyList())
            setMyOfferClickListener(this@MyOffersFragment)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.offeredJobsRecyclerview.itemAnimator = DefaultItemAnimator()
        viewBinding.offeredJobsRecyclerview.adapter = myOfferJobsRecyclerAdapter
        viewBinding.myOfferJobRefresh.setOnRefreshListener {
            viewModel.getMyOffers()
            viewBinding.myOfferJobRefresh.isRefreshing = false
        }
    }

    override fun onInitViewModel() {
        viewModel.myOffersState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> jobsLoading()
                is Lce.Content -> jobsLoaded(it.content)
                is Lce.Error -> errorWhileLoadingJobs(it.error)
            }
        })
        viewModel.acceptJobState.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lse.Loading -> {

                }
                is Lse.Success -> {
                    viewModel.getMyOffers()
                }
                is Lse.Error -> {

                }
            }
        })
        viewModel.getMyOffers()
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        myOfferJobsRecyclerAdapter.setMyOfferJobList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(jobs: List<Application>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (jobs.isEmpty()) {

            myOfferJobsRecyclerAdapter.setMyOfferJobList(jobs)
            showNoJobsFoundLayout()
        } else {

            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            myOfferJobsRecyclerAdapter.setMyOfferJobList(jobs)
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
        myOfferJobsRecyclerAdapter.setMyOfferJobList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.getMyOffers()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == JobDetailsActivity.REQUEST_CODE) {
            viewModel.getMyOffers()
            acceptTaskListener?.onAccept()
        }
    }

    override fun onAcceptOffer(application: Application) {
        viewModel.acceptJob(application.id.toString())
    }

    override fun onJobClick(application: Application) {
        val intent = Intent(requireContext(), JobDetailsActivity::class.java)
        intent.putExtra(JobDetailsActivity.IS_MY_OFFER_FRAGMENT, true)
        intent.putExtra(JobDetailsActivity.JOB_ID, application.jobid.toString())
        intent.putExtra(JobDetailsActivity.APPLICATION_ID, application.id.toString())
        startActivityForResult(intent, JobDetailsActivity.REQUEST_CODE)
    }

    interface AcceptTaskListener {
        fun onAccept()
    }
}