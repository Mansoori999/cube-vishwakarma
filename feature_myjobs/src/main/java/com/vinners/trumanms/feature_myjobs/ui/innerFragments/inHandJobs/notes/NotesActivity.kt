package com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.notes

import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.TextInputLayout
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.notes.Notes
import com.vinners.trumanms.data.models.notes.NotesRequest
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.ActivityNotesBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.InHandJobsViewModel
import javax.inject.Inject

class NotesActivity :
    BaseActivity<ActivityNotesBinding, InHandJobsViewModel>(R.layout.activity_notes) {
    companion object {
        const val INTENT_EXTRA_APPLICATION_ID = "application_id"
        const val INTENT_EXTRA_JOB_TITLE = "job_title"
        const val INTENT_EXTRA_JOB_NO = " job_no"
    }

    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    override val viewModel: InHandJobsViewModel by viewModels { viewModelFactory }

    private var applicationId: String? = null
    private var jobTitle: String? = null
    private var jobNo: String? = null
    private lateinit var submiNotesBtn: Button
    private lateinit var alertDialog: AlertDialog

    private val notesRecyclerAdapter: NotesRecyclerAdapter by lazy {
        NotesRecyclerAdapter().apply {
            updateList(emptyList())
        }
    }

    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        applicationId = intent.getStringExtra(INTENT_EXTRA_APPLICATION_ID)
        jobTitle = intent.getStringExtra(INTENT_EXTRA_JOB_TITLE)
        jobNo = intent.getStringExtra(INTENT_EXTRA_JOB_NO)
        viewBinding.notesRecycler.itemAnimator = DefaultItemAnimator()
        viewBinding.notesRecycler.adapter = notesRecyclerAdapter
        viewBinding.refreshNotesLayout.setOnRefreshListener {
            viewModel.getNotes(applicationId!!)
            viewBinding.refreshNotesLayout.isRefreshing = false
        }

        viewBinding.openNotes.setOnClickListener {
            openNotesLayout()
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }

        viewBinding.jobTitle.text = jobTitle
        viewBinding.jobNo.text = jobNo
    }

    override fun onInitViewModel() {
        viewModel.notesState.observe(this, Observer {
            when (it) {
                Lce.Loading -> jobsLoading()
                is Lce.Content -> jobsLoaded(it.content)
                is Lce.Error -> errorWhileLoadingJobs(it.error)
            }
        })
        viewModel.submitNotesState.observe(this, Observer {
            when(it){
                Lse.loading() -> {
                    submiNotesBtn.showProgress {
                        buttonText = "Loading"
                        progressColor = R.color.white
                    }
                    submiNotesBtn.isEnabled = false
                }
                Lse.Success -> {
                    submiNotesBtn.hideProgress(
                        "Submit"
                    )
                    submiNotesBtn.isEnabled = true
                    Toast.makeText(this,"Successfully Submitted",Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                    viewModel.getNotes(applicationId!!)
                }
                is Lse.Error -> {
                    submiNotesBtn.hideProgress(
                        "Submit"
                    )
                    submiNotesBtn.isEnabled = true
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getNotes(applicationId!!)
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        notesRecyclerAdapter.updateList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(jobs: List<Notes>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (jobs.isEmpty()) {

            notesRecyclerAdapter.updateList(jobs)
            showNoJobsFoundLayout()
        } else {

            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            notesRecyclerAdapter.updateList(jobs)
        }
    }

    private fun showNoJobsFoundLayout() {
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = "No Notes Found"
        viewBinding.jobsInfoLayout.errorActionButton.setVisibilityGone()
    }

    private fun errorWhileLoadingJobs(error: String) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        notesRecyclerAdapter.updateList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.getNotes(applicationId!!)
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun openNotesLayout(){
        val view = LayoutInflater.from(this).inflate(R.layout.open_notes_pop_up_layout, null)
        val reasonContainer = view.findViewById<TextInputLayout>(R.id.notesContainer)
        val notesEt = view.findViewById<EditText>(R.id.notesEt)
        notesEt.doOnTextChanged { text, start, count, after ->
            if (text.isNullOrEmpty().not())
                reasonContainer.error = null
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)
        submiNotesBtn = view.findViewById(R.id.submitNotesBtn)
        submiNotesBtn.setOnClickListener {
            if (notesEt.text.toString().isEmpty())
                reasonContainer.error = "Please Enter Valid Notes"
            else{
                val notes = notesEt.text.toString()
                val noteRequest = NotesRequest(
                    applicationId = applicationId,
                    notes = notes
                )
                viewModel.submitNotes(noteRequest)
            }
        }
        alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}