package com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.core.extensions.selectItemWithText
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.data.models.notes.NotesRequest
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentInHandJobsBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.notes.NotesActivity
import javax.inject.Inject


class InHandJobsFragment :
    BaseFragment<FragmentInHandJobsBinding, InHandJobsViewModel>(R.layout.fragment_in_hand_jobs),
    InHandJobClickListener {

    companion object {
        const val TAG = "InHandJobsFragment"
    }

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo
    lateinit var alertDialog: AlertDialog
    lateinit var categorySpinner: Spinner
    lateinit var submitAbandonTask: Button
    private lateinit var submiStatusBtn: Button
    private lateinit var statusAlertDialog: AlertDialog
    private var isChangeStatusbtn: Boolean = false

    override val viewModel: InHandJobsViewModel by viewModels { viewModelFactory }

    private val inHandRecyclerAdapter: InHandJobRecyclerAdapter by lazy {
        InHandJobRecyclerAdapter(appInfo).apply {
            setInHandJobList(emptyList())
            setListener(this@InHandJobsFragment)
        }
    }


    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.inHandJobsRecyclerview.itemAnimator = DefaultItemAnimator()
        viewBinding.inHandJobsRecyclerview.adapter = inHandRecyclerAdapter
        viewBinding.inHandJobRefresh.setOnRefreshListener {
            viewModel.getInHandJobsList()
            viewBinding.inHandJobRefresh.isRefreshing = false
        }
    }

    override fun onInitViewModel() {
        viewModel.inHandJobsState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lce.Loading -> jobsLoading()
                is Lce.Content -> jobsLoaded(it.content)
                is Lce.Error -> errorWhileLoadingJobs(it.error)
            }
        })

        viewModel.abandonTaskState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lse.Loading -> {
                    submitAbandonTask.showProgress {
                        buttonText = "Loading"
                        progressColor = R.color.white
                    }
                    submitAbandonTask.isEnabled = false

                }
                Lse.Success -> {

                    submitAbandonTask.hideProgress(
                        "Submit"
                    )
                    submitAbandonTask.isEnabled = true
                    alertDialog.dismiss()

                    Toast.makeText(requireContext(), "Submitted Succeccfully", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getInHandJobsList()
                }
                is Lse.Error -> {

                    submitAbandonTask.hideProgress(
                        "Submit"
                    )
                    submitAbandonTask.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.statusTaskState.observe(viewLifecycleOwner, Observer {
            when (it) {
                Lse.Loading -> {
                    submiStatusBtn.showProgress {
                        buttonText = "Loading"
                        progressColor = R.color.white
                    }
                    submiStatusBtn.isEnabled = false
                }
                Lse.Success -> {
                    submiStatusBtn.hideProgress(
                        "Submit"
                    )
                    submiStatusBtn.isEnabled = true
                    statusAlertDialog.dismiss()
                    Toast.makeText(requireContext(), "Submitted Succeccfully", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.getInHandJobsList()
                }
                is Lse.Error -> {
                    submiStatusBtn.hideProgress(
                        "Submit"
                    )
                    submiStatusBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getInHandJobsList()
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        inHandRecyclerAdapter.setInHandJobList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(jobs: List<Application>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (jobs.isEmpty()) {

            inHandRecyclerAdapter.setInHandJobList(jobs)
            showNoJobsFoundLayout()
        } else {

            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            inHandRecyclerAdapter.setInHandJobList(jobs)
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
        inHandRecyclerAdapter.setInHandJobList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.getInHandJobsList()
        }
    }

    override fun onCloseClick(application: Application) {
        val view =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.task_complete_in_hand_job_layout, null)
        val categorySpinner = view.findViewById<Spinner>(R.id.categorySpinner)
        submitAbandonTask = view.findViewById(R.id.submitBtn)
        val query = view.findViewById<TextInputEditText>(R.id.queryEt)
        val queryLayout = view.findViewById<TextInputLayout>(R.id.queryLayout)
        val abandonLayout = view.findViewById<LinearLayout>(R.id.abandonLayout)
        val abandonBtn = view.findViewById<Button>(R.id.abandonBtn)
        val resumeBtn = view.findViewById<Button>(R.id.resumeBtn)
        abandonBtn.setOnClickListener {
            abandonLayout.setVisibilityVisible()
        }
        resumeBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (categorySpinner.selectedItem.toString().equals("others", true))
                    queryLayout.setVisibilityVisible()
                else
                    queryLayout.setVisibilityGone()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        query.doOnTextChanged { text, start, count, after ->
            if (queryLayout.error != null)
                queryLayout.error = null
        }
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(view)
        submitAbandonTask.setOnClickListener {
            isChangeStatusbtn = false
            val reason = if (categorySpinner.selectedItem.toString().equals("others", true))
                query.text.toString()
            else
                categorySpinner.selectedItem.toString()

            if (reason.isEmpty())
                showInformationDialog("Enter valid Reason")
            else {
                viewModel.abandonTask(application.id.toString(), "abandon", reason)
            }
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDailyNotesClick(application: Application) {
        val intent = Intent(requireContext(), NotesActivity::class.java)
        intent.putExtra(NotesActivity.INTENT_EXTRA_APPLICATION_ID, application.id.toString())
        intent.putExtra(NotesActivity.INTENT_EXTRA_JOB_TITLE, application.title)
        intent.putExtra(NotesActivity.INTENT_EXTRA_JOB_NO, application.jobno)
        startActivity(intent)
    }

    override fun onStatusChange(application: Application) {
        openTaskStatusLayout(application)
    }

    fun openTaskStatusLayout(application: Application) {
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.job_status_pop_up_layout, null)
        val statusSpinner = view.findViewById<Spinner>(R.id.statusSpinner)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(view)
        if (application.jobStatus.isNullOrEmpty().not())
            statusSpinner.selectItemWithText(application.jobStatus!!)
        submiStatusBtn = view.findViewById(R.id.submitStatusBtn)
        submiStatusBtn.setOnClickListener {
            isChangeStatusbtn = true
            val status = statusSpinner.selectedItem.toString()
            viewModel.updateStatus(application.id.toString(), "finish", status)
        }
        statusAlertDialog = alertDialogBuilder.create()
        statusAlertDialog.show()
    }
}