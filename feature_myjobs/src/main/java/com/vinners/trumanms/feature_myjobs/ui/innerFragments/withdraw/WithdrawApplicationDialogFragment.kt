package com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityInvisible
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentWithdrawApplicationDialogBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import javax.inject.Inject

class WithdrawApplicationDialogFragment :
    BaseDialogFragment<FragmentWithdrawApplicationDialogBinding, WithdrawApplicationViewModel>(R.layout.fragment_withdraw_application_dialog) {

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    private lateinit var jobId: String
    private lateinit var applicationId: String
    private lateinit var mJobApplicationDialogFragmentEventListener: WithdrawApplicationDialogFragmentEventListener

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {

            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override val viewModel: WithdrawApplicationViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {

        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            jobId = it.getString(INTENT_EXTRA_JOB_ID) ?: return@let
            applicationId = it.getString(INTENT_EXTRA_APPLICATION_ID) ?: return@let
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onInitDataBinding() {
        viewBinding.withdrawMainLayout.submitBtn.setOnClickListener {

            viewModel.withdrawFromJob(
                applicationId = applicationId,
                jobId = jobId,
                withdrawalReason = viewBinding.withdrawMainLayout.withdrawalReasonEt.text.toString()
            )
        }
    }

    override fun onInitViewModel() {
        viewModel.viewState
            .observe(viewLifecycleOwner, Observer {

                when (it) {
                    is WithdrawApplicationViewModelStates.WithDrawingFromJob -> showProgressDialog()
                    is WithdrawApplicationViewModelStates.WithDrawnFromJob -> withDrawnFromJob(it.jodId)
                    is WithdrawApplicationViewModelStates.ErrorWhileWithDrawingFromJob -> showErrorWhileWthDrawingJob(
                        it.error
                    )
                }
            })
    }

    private fun showErrorWhileWthDrawingJob(error: String) {
        viewBinding.withdrawMainLayout.root.setVisibilityVisible()
        viewBinding.progressBar.setVisibilityGone()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Alert")
            .setMessage("Unable to Submit Withdrawing from Job, $error")
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun withDrawnFromJob(jobId: String) {

        mJobApplicationDialogFragmentEventListener.jobApplicationWithdrawn(jobId)
        dismiss()
    }

    private fun showProgressDialog() {
        viewBinding.withdrawMainLayout.root.setVisibilityInvisible()
        viewBinding.progressBar.setVisibilityVisible()
    }

    companion object {

        private const val INTENT_EXTRA_JOB_ID = "job_id"
        private const val INTENT_EXTRA_APPLICATION_ID = "application_id"
        const val TAG = "WithdrawApplicationDialogFragment"

        fun launch(
            jobId: String,
            applicationId: String,
            fragmentManager: FragmentManager,
            jobWithdrawalListener: WithdrawApplicationDialogFragmentEventListener
        ) {
            val frag = WithdrawApplicationDialogFragment()
            frag.arguments = bundleOf(
                INTENT_EXTRA_JOB_ID to jobId,
                INTENT_EXTRA_APPLICATION_ID to applicationId
            )
            frag.mJobApplicationDialogFragmentEventListener = jobWithdrawalListener
            frag.show(fragmentManager, TAG)
        }
    }
}

interface WithdrawApplicationDialogFragmentEventListener {

    fun jobApplicationWithdrawn(jobId: String)
}