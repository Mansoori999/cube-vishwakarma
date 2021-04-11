package com.vinners.trumanms.feature_job.ui.jobList.jobDetails

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import coil.api.load
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.JobDetails
import com.vinners.trumanms.feature_job.R
import com.vinners.trumanms.feature_job.databinding.ActivityJobDetailsBinding
import com.vinners.trumanms.feature_job.di.DaggerJobsComponent
import com.vinners.trumanms.feature_job.di.JobsViewModelFactory
import javax.inject.Inject

class JobDetailsActivity :
    BaseActivity<ActivityJobDetailsBinding, JobDetailsViewModel>(R.layout.activity_job_details) {
    companion object {
        const val REQUEST_CODE = 100
        const val PATH_PREFIX_TOKEN = "token"
        const val JOB_ID = "jobId"
        const val APPLICATION_ID = "applicationId"
        const val IS_APPLIED_FRAGMENT = "appliedFragment"
        const val IS_MY_OFFER_FRAGMENT = "myOfferFragment"
        const val IS_SAVE_FRAGMENT = "saveFragment"
        const val IS_FROM_JOB_HISTORY = "job_history"
        const val JOB_HISTORY_STATUS = "jobHistoryStatus"
        const val TASK_MODE_PER_TASK = "Per Task"
        const val TASK_MODE_ON_PERIOD = "On Period"
    }

    @Inject
    lateinit var viewModelFactory: JobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo

    private var shareToken: String? = null
    private var jobId: String? = null
    private var unsaveJobId: String? = null
    private var applicationId: String? = null
    private var isAppliedFragment: Boolean = false
    private var isSavedFragment: Boolean = false
    private var isMyOfferFragment: Boolean = false
    private lateinit var submitWithdrawBtn: Button
    private var isJobHistory: Boolean = false
    private var jobHistoryStatus: String? = null
    private lateinit var alertDialog: MaterialAlertDialogBuilder
    override val viewModel: JobDetailsViewModel by viewModels {
        viewModelFactory
    }

    override fun onInitDependencyInjection() {
        DaggerJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        jobId = intent.getStringExtra(JOB_ID)
        applicationId = intent.getStringExtra(APPLICATION_ID)
        isAppliedFragment = intent.getBooleanExtra(IS_APPLIED_FRAGMENT, false)
        isSavedFragment = intent.getBooleanExtra(IS_SAVE_FRAGMENT, false)
        isMyOfferFragment = intent.getBooleanExtra(IS_MY_OFFER_FRAGMENT, false)
        isJobHistory = intent.getBooleanExtra(IS_FROM_JOB_HISTORY, false)
        jobHistoryStatus = intent.getStringExtra(JOB_HISTORY_STATUS)
        getDeepLink()
        if (isJobHistory) {
            viewBinding.applyJobBtn.setVisibilityGone()
            viewBinding.saveJobBtn.setVisibilityGone()
            viewBinding.acceptJobBtn.setVisibilityGone()
            viewBinding.withdrawJobBtn.setVisibilityGone()
            viewBinding.unsaveJobBtn.setVisibilityGone()
            viewBinding.workStatusLayout.setVisibilityVisible()
            viewBinding.workStatus.text = jobHistoryStatus
        }
        viewBinding.applyJobBtn.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog_layout, null)
            val message = view.findViewById<TextView>(R.id.messageEt)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)
            message.text = "Sure to apply?"
            var alertDialog: AlertDialog? = null
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(view)

            yesBtn.setOnClickListener {
                viewModel.applyJob(jobId!!)
            }
            noBtn.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog = alertDialogBuilder.create()
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(alertDialog.window?.attributes)
            layoutParams.height = (DisplayMetrics().heightPixels * 0.7f).toInt()
            layoutParams.width = (DisplayMetrics().widthPixels).toInt()
            alertDialog.window?.attributes = layoutParams
            alertDialog?.show()
        }
        viewBinding.saveJobBtn.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog_layout, null)
            val message = view.findViewById<TextView>(R.id.messageEt)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)
            message.text = "Sure to save?"
            var alertDialog: AlertDialog? = null
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(view)
            yesBtn.setOnClickListener {
                viewModel.saveJob(jobId!!)
            }
            noBtn.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.show()
        }
        viewBinding.acceptJobBtn.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog_layout, null)
            val message = view.findViewById<TextView>(R.id.messageEt)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)
            message.text = "Sure to accept?"
            var alertDialog: AlertDialog? = null
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(view)
            yesBtn.setOnClickListener {
                viewModel.acceptJob(applicationId!!)
            }
            noBtn.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.show()
        }
        viewBinding.passJobBtn.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog_layout, null)
            val message = view.findViewById<TextView>(R.id.messageEt)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)
            message.text = "Sure to Pass?"
            var alertDialog: AlertDialog? = null
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(view)
            yesBtn.setOnClickListener {
                viewModel.passJob(applicationId!!)
            }
            noBtn.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.show()
        }
        viewBinding.unsaveJobBtn.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog_layout, null)
            val message = view.findViewById<TextView>(R.id.messageEt)
            val yesBtn = view.findViewById<Button>(R.id.yesBtn)
            val noBtn = view.findViewById<Button>(R.id.noBtn)
            message.text = "Sure to unsave?"
            var alertDialog: AlertDialog? = null
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setView(view)
            yesBtn.setOnClickListener {
                viewModel.unSaveJob(unsaveJobId!!)
            }
            noBtn.setOnClickListener {
                alertDialog?.dismiss()
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog?.show()
        }
        viewBinding.withdrawJobBtn.setOnClickListener {
            openAlertDialogForJobWithdraw()
        }
        viewBinding.shareJob.setOnClickListener {
            viewModel.createShareLink()
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onInitViewModel() {
        viewModel.jobDetailsState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    setDataOnView(it.content)
                }
                is Lce.Error -> {
                    Toast.makeText(this,it.error,Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.applyJobState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.applyJobBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.applyJobBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.applyJobBtn.hideProgress(

                    )
                    // viewModel.getJobDetails(jobId!!)
                    Toast.makeText(this, "Applied Successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                is Lse.Error -> {
                    viewBinding.applyJobBtn.hideProgress(
                        R.string.apply
                    )
                    viewBinding.applyJobBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.saveJobState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.saveJobBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.saveJobBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.saveJobBtn.hideProgress(
                        "Saved"
                    )
                    //viewModel.getJobDetails(jobId!!)
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                is Lse.Error -> {
                    viewBinding.saveJobBtn.hideProgress(
                        R.string.save
                    )
                    viewBinding.saveJobBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.unsaveJobState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.unsaveJobBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.unsaveJobBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.unsaveJobBtn.hideProgress(
                        "Unsaved"
                    )
                    onBackPressed()
                    Toast.makeText(this, "Unsaved Successfully", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {
                    viewBinding.unsaveJobBtn.hideProgress(
                        "Unsave"
                    )
                    viewBinding.unsaveJobBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.withdrawApplicationState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    submitWithdrawBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    submitWithdrawBtn.isEnabled = false
                }
                is Lse.Success -> {
                    submitWithdrawBtn.hideProgress(
                        "Submit"
                    )
                    onBackPressed()
                    Toast.makeText(this, "Withdrawn Successfully", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {
                    submitWithdrawBtn.hideProgress(
                        "Submit"
                    )
                    submitWithdrawBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.acceptJobState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.acceptJobBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.acceptJobBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.acceptJobBtn.hideProgress(
                        "Accepted"
                    )
                    onBackPressed()
                    Toast.makeText(this, "Accepted Successfully", Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {
                    viewBinding.acceptJobBtn.hideProgress(
                        "Accept"
                    )
                    viewBinding.acceptJobBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.passState.observe(this, Observer {
            when (it) {
                Lse.Loading -> {
                    viewBinding.passJobBtn.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColor = Color.WHITE
                    }
                    viewBinding.passJobBtn.isEnabled = false
                }
                is Lse.Success -> {
                    viewBinding.passJobBtn.hideProgress(
                        "Passed"
                    )
                    Toast.makeText(this, "Passed Successfully", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                    viewBinding.passJobBtn.isEnabled = true
                }
                is Lse.Error -> {
                    viewBinding.passJobBtn.hideProgress(
                        "Pass"
                    )
                    viewBinding.passJobBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.shareLinkState.observe(this, Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    showShareDialog(it.content)
                }
                is Lce.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getJobDetails(jobId, shareToken)
    }

    fun setDataOnView(jobDetails: JobDetails) {
        unsaveJobId = jobDetails.saveJobId.toString()
        if (isAppliedFragment) {
            viewBinding.saveJobBtn.setVisibilityGone()
            viewBinding.applyJobBtn.setVisibilityGone()
            viewBinding.unsaveJobBtn.setVisibilityGone()
            viewBinding.withdrawJobBtn.setVisibilityVisible()
        }
        if (isSavedFragment) {
            viewBinding.saveJobBtn.setVisibilityGone()
            viewBinding.unsaveJobBtn.setVisibilityVisible()
        }
        if (isMyOfferFragment) {
            viewBinding.saveJobBtn.setVisibilityGone()
            viewBinding.applyJobBtn.setVisibilityGone()
            viewBinding.passJobBtn.setVisibilityVisible()
            viewBinding.acceptJobBtn.setVisibilityVisible()
        }
        viewBinding.campaignEt.text = jobDetails.campaignname
        viewBinding.campaignIdEt.text = jobDetails.campaignNo
        viewBinding.jobTitleEt.text = jobDetails.title
        viewBinding.jobIdEt.text = jobDetails.jobno
        viewBinding.jobLocationEt.text = "${jobDetails.city}, ${jobDetails.state}"
        viewBinding.workPeroidEt.text = "Work Period: ${jobDetails.workPeriod}"
        if (jobDetails.lastdatetoapply.isNullOrEmpty().not())
            viewBinding.lastDateApplyEt.text =
                "Last Date to apply: ${DateTimeHelper.getFancyDateFromString(jobDetails.lastdatetoapply)}"

        if (jobDetails.logo.isNullOrEmpty().not())
            viewBinding.JobIcon.load(appInfo.getFullAttachmentUrl(jobDetails.logo!!))

        if (jobDetails.twowheelermust.not()) {
            viewBinding.isTwoWeelarLayout.setVisibilityGone()
            viewBinding.certificateView.setVisibilityVisible()
        }
        if (jobDetails.workfromhome.not()) {
            viewBinding.isWorkFromLayout.setVisibilityGone()
            viewBinding.certificateView.setVisibilityVisible()
        }
        if (jobDetails.certificateissue.not()) {
            viewBinding.isCertificateLayout.setVisibilityGone()
            viewBinding.certificateView.setVisibilityVisible()
        }
        if (jobDetails.twowheelermust.not() && jobDetails.workfromhome.not() && jobDetails.certificateissue.not())
            viewBinding.workFromLayout.setVisibilityGone()

        if (jobDetails.isapplied == 1) {
            viewBinding.applyJobBtn.isEnabled = false
            viewBinding.unsaveJobBtn.setVisibilityGone()
            viewBinding.applyJobBtn.text = "Applied"
        } else {
            viewBinding.applyJobBtn.isEnabled = true
            viewBinding.applyJobBtn.text = getString(R.string.apply)
        }
        if (jobDetails.issaved == 1 && isSavedFragment) {
            viewBinding.saveJobBtn.setVisibilityGone()
            viewBinding.unsaveJobBtn.setVisibilityVisible()
        } else {
            viewBinding.saveJobBtn.isEnabled = true
            viewBinding.saveJobBtn.text = getString(R.string.save)
        }
        if (jobDetails.earningMode.equals(TASK_MODE_PER_TASK, true)) {
            viewBinding.totalEarningTitle.setVisibilityVisible()
            viewBinding.totalEarningEt.setVisibilityVisible()
            viewBinding.totalEarningEt.text = "\u20B9${jobDetails.earningpertask}"
        } else if (jobDetails.earningMode.equals(TASK_MODE_ON_PERIOD,true)) {
            viewBinding.salaryTitle.setVisibilityVisible()
            viewBinding.salaryEt.setVisibilityVisible()
            viewBinding.salaryEt.text = "${jobDetails.minSalary}-${jobDetails.maxSalary} PM"
        }
        viewBinding.descriptionEt.text = jobDetails.description
        viewBinding.brandet.text = jobDetails.aboutbrand
        viewBinding.openingEt.text = jobDetails.resourcecount.toString()
        viewBinding.experienceEt.text = jobDetails.minexperience
        viewBinding.qualificationEt.text = jobDetails.minqualification
        viewBinding.referLinkEt.text = jobDetails.reflink
        viewBinding.paymentSourceEt.text = jobDetails.paymentSource
        if (jobDetails.startdate.isNullOrEmpty().not())
            viewBinding.startDateEt.text =
                DateTimeHelper.getFancyDateFromString(jobDetails.startdate)
    }

    private fun showShareDialog(it: ShareInfo) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, it.subject)
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "See tasks at brands that need you \n ${it.body}")
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    fun openAlertDialogForJobWithdraw() {
        val view = LayoutInflater.from(this).inflate(R.layout.withdraw_reason_layout, null)
        val reasonContainer = view.findViewById<TextInputLayout>(R.id.reasonContainer)
        val reason = view.findViewById<EditText>(R.id.reasonEt)
        reason.doOnTextChanged { text, start, count, after ->
            if (text.isNullOrEmpty().not())
                reasonContainer.error = null
        }
        alertDialog = MaterialAlertDialogBuilder(this)
        alertDialog.setView(view)
        submitWithdrawBtn = view.findViewById(R.id.submitJobBtn)
        submitWithdrawBtn.setOnClickListener {
            if (reason.text.toString().isEmpty())
                reasonContainer.error = "Please Enter Valid Reason"
            else
                viewModel.withdrawJob(applicationId!!, reason.text.toString())
        }
        alertDialog.show()
    }

    fun openAlertDialogForConfirmation(message: String) {

    }

    private fun getDeepLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener {
                if (it != null) {
                    shareToken = it.link?.getQueryParameter(PATH_PREFIX_TOKEN)

                    if (shareToken != null)
                        viewModel.getJobDetails(jobId, shareToken)
                }

            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}