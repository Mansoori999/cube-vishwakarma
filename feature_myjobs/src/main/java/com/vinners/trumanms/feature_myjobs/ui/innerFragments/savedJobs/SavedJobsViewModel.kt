package com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.data.models.jobs.SavedJob
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class SavedJobsViewModelStates {

    object LoadingJobs : SavedJobsViewModelStates()

    class SavedJobsLoaded(val jobs: List<SavedJob>) : SavedJobsViewModelStates()

    class ErrorWhileFetchingJobs(val error: String) : SavedJobsViewModelStates()


    object ApplyingJob : SavedJobsViewModelStates()

    class AppliedForJob(val jobId: String) : SavedJobsViewModelStates()

    class ErrorWhileApplyingJob(val jobId: String) : SavedJobsViewModelStates()
}

class SavedJobsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<SavedJobsViewModelStates> = MutableLiveData()
    val viewState: LiveData<SavedJobsViewModelStates> = _viewState

    fun getSavedJob() = viewModelScope.launch {
        _viewState.value = SavedJobsViewModelStates.LoadingJobs

        try {
            val jobs = jobsRepository.getSavedJobs()
            _viewState.value = SavedJobsViewModelStates.SavedJobsLoaded(jobs)
        } catch (e: Exception) {
            Timber.e(e, "Error while fetching Saved Jobs")
            _viewState.value = SavedJobsViewModelStates.ErrorWhileFetchingJobs(
                e.message ?: "Unable to fetch saved jobs"
            )
        }
    }

    fun applyForJob(jobId : String) = viewModelScope.launch {
        _viewState.value = SavedJobsViewModelStates.ApplyingJob

        try {
            jobsRepository.applyJob(jobId)
            _viewState.value = SavedJobsViewModelStates.AppliedForJob(jobId)
        } catch (e: Exception) {
            Timber.e(e, "Error while Applying For Job")
            _viewState.value = SavedJobsViewModelStates.ErrorWhileApplyingJob(
                e.message ?: "Error while Applying For Job"
            )
        }
    }
}