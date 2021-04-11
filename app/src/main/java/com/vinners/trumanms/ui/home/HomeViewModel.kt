package com.vinners.trumanms.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.data.models.jobs.Job
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.repository.JobsRepository
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import com.vinners.trumanms.feature.auth.ui.ObserveProfileInteractor
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface JobsViewModelEvents {
    val jobListState: LiveData<Lce<List<Job>>>

    val getApplicationState: LiveData<Lce<List<Application>>>

    val profile: LiveData<Profile>

    fun initViewModel()
}

class HomeViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val jobRepository: JobsRepository,
    private val observeProfileInteractor: ObserveProfileInteractor
) : AuthViewModel(userSessionManager), JobsViewModelEvents {

    override fun initViewModel() {
        startObservingProfile()
    }

    private val _profile = MutableLiveData<Profile>()
    override val profile: LiveData<Profile> = _profile

    private val _jobListState = MutableLiveData<Lce<List<Job>>>()
    override val jobListState: LiveData<Lce<List<Job>>> = _jobListState


    private val _getApplicationState = MutableLiveData<Lce<List<Application>>>()
    override val getApplicationState: LiveData<Lce<List<Application>>> = _getApplicationState

    private fun startObservingProfile() {
        observeProfileInteractor.execute(GetProfileObserver(), null)
    }

    private inner class GetProfileObserver : DisposableObserver<Profile>() {

        override fun onComplete() {}

        override fun onError(e: Throwable) {}

        override fun onNext(t: Profile) {
            _profile.postValue(t)
        }
    }

    fun getJoblist(pageNo: Int, cityId: String) {
        _jobListState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = jobRepository.getJobList(pageNo, cityId)
                _jobListState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _jobListState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }


    fun getApplication() {
        _getApplicationState.value = Lce.loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
//                val response = jobRepository.getApplications()
//                _getApplicationState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _getApplicationState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        observeProfileInteractor.dispose()
    }
}