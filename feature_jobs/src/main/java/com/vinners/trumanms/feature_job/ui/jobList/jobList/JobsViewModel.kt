package com.vinners.trumanms.feature_job.ui.jobList.jobList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.jobs.Job
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.data.repository.AuthRepository
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
interface JobEvents{
    val jobListState: LiveData<Lce<List<Job>>>
    val state: LiveData<Lce<List<State>>>
}
class JobsViewModel @Inject constructor(
    private val jobRepository: JobsRepository,
    private val authRepository: AuthRepository
) : ViewModel(),JobEvents {

    private val _jobListState = MutableLiveData<Lce<List<Job>>>()
    override val jobListState: LiveData<Lce<List<Job>>> = _jobListState

    private val _state = MutableLiveData<Lce<List<State>>>()
    override val state: LiveData<Lce<List<State>>> = _state

    fun getJobs(
        pageNo: Int,
        cityId: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        _jobListState.postValue(Lce.Loading)

        try {
            val response = jobRepository.getJobList(pageNo, cityId)
            _jobListState.postValue(Lce.Content(response))
        } catch (e: Exception) {
            _jobListState.postValue(Lce.Error(e.localizedMessage))
        }
    }
    fun getState() {
        _state.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.getState()
                _state.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _state.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}