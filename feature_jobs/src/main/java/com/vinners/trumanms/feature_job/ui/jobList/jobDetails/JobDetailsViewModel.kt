package com.vinners.trumanms.feature_job.ui.jobList.jobDetails

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.JobDetails
import com.vinners.trumanms.data.models.jobs.JobDetailsShareLink
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShareInfo(
    val subject: String,
    val body: String,
    val image: Uri
)

interface JobDetailsEvents {
    val jobDetailsState: LiveData<Lce<JobDetails>>

    val saveJobState: LiveData<Lse>

    val applyJobState: LiveData<Lse>

    val withdrawApplicationState: LiveData<Lse>

    val acceptJobState: LiveData<Lse>

    val unsaveJobState: LiveData<Lse>

    val shareLinkState: LiveData<Lce<ShareInfo>>

    val passState: LiveData<Lse>
}

class JobDetailsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository,
    private val appInfo: AppInfo
) : ViewModel(), JobDetailsEvents {

    private var jobDetails: JobDetails? = null

    private val _jobDetailsState = MutableLiveData<Lce<JobDetails>>()
    override val jobDetailsState: LiveData<Lce<JobDetails>> = _jobDetailsState

    private val _saveJobState = MutableLiveData<Lse>()
    override val saveJobState: LiveData<Lse> = _saveJobState

    private val _applyJobState = MutableLiveData<Lse>()
    override val applyJobState: LiveData<Lse> = _applyJobState

    private val _withdrawApplicationState = MutableLiveData<Lse>()
    override val withdrawApplicationState: LiveData<Lse> = _withdrawApplicationState

    private val _acceptJobState = MutableLiveData<Lse>()
    override val acceptJobState: LiveData<Lse> = _acceptJobState

    private val _unsaveJobState = MutableLiveData<Lse>()
    override val unsaveJobState: LiveData<Lse> = _unsaveJobState

    private val _passState = MutableLiveData<Lse>()
    override val passState: LiveData<Lse> = _passState

    private val _shareLinkState = MutableLiveData<Lce<ShareInfo>>()
    override val shareLinkState: LiveData<Lce<ShareInfo>> = _shareLinkState

    fun saveJob(jobId: String) {
        viewModelScope.launch {
            _saveJobState.value = Lse.loading()
            try {
                jobsRepository.saveJob(jobId)
                _saveJobState.postValue(Lse.Success)
            } catch (e: Exception) {
                _saveJobState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun applyJob(jobId: String) {
        viewModelScope.launch {
            _applyJobState.value = Lse.Loading
            try {
                jobsRepository.applyJob(jobId)
                _applyJobState.postValue(Lse.Success)
            } catch (e: Exception) {
                _applyJobState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun getJobDetails(jobId: String?, shareToken: String?) {
        viewModelScope.launch {
            _jobDetailsState.value = Lce.Loading
            try {
                jobDetails = jobsRepository.getJobDetails(jobId, shareToken)
                _jobDetailsState.postValue(Lce.Content(jobDetails!!))
            } catch (e: Exception) {
                _jobDetailsState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun withdrawJob(jobId: String, reason: String) {
        viewModelScope.launch {
            _withdrawApplicationState.value = Lse.Loading
            try {
                jobsRepository.withdrawApplication(jobId, reason)
                _withdrawApplicationState.postValue(Lse.Success)
            } catch (e: Exception) {
                _withdrawApplicationState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun acceptJob(applicationId: String) {
        _acceptJobState.value = Lse.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                jobsRepository.acceptJob(applicationId)
                _acceptJobState.postValue(Lse.Success)
            } catch (e: Exception) {
                _acceptJobState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun passJob(applicationId: String) {
        viewModelScope.launch {
            _passState.value = Lse.Loading
            try {
                jobsRepository.passTask(applicationId, "pass")
                _passState.postValue(Lse.Success)
            } catch (e: Exception) {
                _passState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun unSaveJob(jobId: String) {
        viewModelScope.launch {
            _unsaveJobState.value = Lse.Loading
            try {
                jobsRepository.unSaveJob(jobId)
                _unsaveJobState.postValue(Lse.Success)
            } catch (e: Exception) {
                _unsaveJobState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun createShareLink() {
        viewModelScope.launch {
            _shareLinkState.value = Lce.Loading
            try {
                val url = jobsRepository.createShareLink(
                    JobDetailsShareLink(
                        jobId = jobDetails?.id.toString(),
                        jobTitle = jobDetails?.title,
                        description = jobDetails?.description,
                        cityName = jobDetails?.city,
                        packageName = appInfo.packageName,
                        companyName = jobDetails?.clientname
                    )
                )
                val title = "Interview for ${jobDetails?.title}"
                val body = "${url}"
                val shareInfo = ShareInfo(
                    subject = title,
                    body = body,
                    image = Uri.parse("")
                )
                _shareLinkState.postValue(Lce.Content(shareInfo))
            } catch (e: Exception) {
                _shareLinkState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}