package com.example.feature_profile.ui.jobHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.jobHistory.JobHistory
import com.vinners.trumanms.data.repository.ProfileRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

interface JobHistoryEvents {
    val jobHistoryState: LiveData<Lce<List<JobHistory>>>
}

class JobHistoryViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel(),JobHistoryEvents {

    private val _jobHistoryState = MutableLiveData<Lce<List<JobHistory>>>()
    override val jobHistoryState: LiveData<Lce<List<JobHistory>>> = _jobHistoryState

    fun getJobHistory(pageNo: Int){
        viewModelScope.launch {
            _jobHistoryState.value = Lce.Loading
            try {
                val response = profileRepository.getJobHistory(pageNo)
                _jobHistoryState.postValue(Lce.Content(response))
            }catch (e: Exception){
                _jobHistoryState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }
}