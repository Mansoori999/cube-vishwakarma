package com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.data.models.jobs.AppliedJob
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AppliedJobsViewModelStates {

    object LoadingJobs : AppliedJobsViewModelStates()

    class AppliedJobsLoaded(val jobs: List<AppliedJob>) : AppliedJobsViewModelStates()

    class ErrorWhileFetchingJobs(val error: String) : AppliedJobsViewModelStates()
}

class AppliedJobsViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel() {

    private val _viewState: MutableLiveData<AppliedJobsViewModelStates> = MutableLiveData()
    val viewState : LiveData<AppliedJobsViewModelStates> = _viewState

    fun fetchAppliedJobs() = viewModelScope.launch {
        _viewState.value = AppliedJobsViewModelStates.LoadingJobs

        try {
            val appliedJobs = jobsRepository.getAppliedJobs()
            _viewState.value = AppliedJobsViewModelStates.AppliedJobsLoaded(appliedJobs)
        } catch (e: Exception) {
            _viewState.value = AppliedJobsViewModelStates.ErrorWhileFetchingJobs(
                e.localizedMessage ?: "Error While Loading Jobs"
            )
        }
    }

}