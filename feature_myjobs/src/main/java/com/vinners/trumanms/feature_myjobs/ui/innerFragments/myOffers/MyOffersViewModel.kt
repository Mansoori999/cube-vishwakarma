package com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.data.repository.JobsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface MyOffersEvents {
    val myOffersState: LiveData<Lce<List<Application>>>
    val acceptJobState: LiveData<Lse>
}

class MyOffersViewModel @Inject constructor(
    private val jobsRepository: JobsRepository
) : ViewModel(), MyOffersEvents {

    private val _myOffersState = MutableLiveData<Lce<List<Application>>>()
    override val myOffersState: LiveData<Lce<List<Application>>> = _myOffersState

    private val _acceptJobState = MutableLiveData<Lse>()
    override val acceptJobState: LiveData<Lse> = _acceptJobState

    fun getMyOffers() {

        viewModelScope.launch {
            _myOffersState.value = Lce.loading()
            try {
                val response = jobsRepository.getMyOffers()
                _myOffersState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _myOffersState.postValue(Lce.Error(e.localizedMessage))
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
}