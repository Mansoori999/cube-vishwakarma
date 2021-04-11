package com.example.feature_profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.profile.UserProfile
import com.vinners.trumanms.data.repository.ProfileRepository
import com.vinners.trumanms.feature.auth.ui.ObserveProfileInteractor
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileEvents {
    val profileState: LiveData<Lce<UserProfile>>

    val profile: LiveData<Profile>

    val percentageProfileCompleteState: LiveData<Int>

    val logoutState: LiveData<Lse>

    val profilePicState: LiveData<Lse>

    fun initViewModel()
}

class ProfileViewModel @Inject constructor(
    private val userProfileRepository: ProfileRepository,
    private val observeProfileInteractor: ObserveProfileInteractor,
    private val observeProfileCompletePercentage: ObserveProfileCompletePercentage
) : ViewModel(), ProfileEvents {

    override fun initViewModel() {
        startObservingProfile()
    }

    private val _profile = MutableLiveData<Profile>()
    override val profile: LiveData<Profile> = _profile

    private val _profilePercentageComplete = MutableLiveData<Int>()
    override val percentageProfileCompleteState: LiveData<Int> = _profilePercentageComplete

    private val _profileState = MutableLiveData<Lce<UserProfile>>()
    override val profileState: LiveData<Lce<UserProfile>> = _profileState

    private val _logoutState = MutableLiveData<Lse>()
    override val logoutState: LiveData<Lse> = _logoutState

    private val _profilePicState = MutableLiveData<Lse>()
    override val profilePicState: LiveData<Lse> = _profilePicState

    private fun startObservingProfile() {
        observeProfileInteractor.execute(GetProfileObserver(), null)
        observeProfileCompletePercentage.execute(GetProfileCompletePercentageObserver(),null)
    }

    private inner class GetProfileObserver : DisposableObserver<Profile>() {

        override fun onComplete() {}

        override fun onError(e: Throwable) {}

        override fun onNext(t: Profile) {
            _profile.postValue(t)
        }
    }

    private inner class GetProfileCompletePercentageObserver : DisposableObserver<Int>() {

        override fun onComplete() {}

        override fun onError(e: Throwable) {}

        override fun onNext(t: Int) {
            _profilePercentageComplete.postValue(t)
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            _profileState.value = Lce.Loading
            try {
                val response = userProfileRepository.getProfile()
                _profileState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _profileState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun updateProfilePic(imageUrl: String?) {
        viewModelScope.launch {
            _profilePicState.value = Lse.Loading
            try {
                userProfileRepository.updateProfilePic(imageUrl)
                _profilePicState.postValue(Lse.Success)
            } catch (e: Exception) {
                _profilePicState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    fun logout() {
        _logoutState.value = Lse.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userProfileRepository.logout()
                _logoutState.postValue(Lse.Success)
            } catch (e: Exception) {
                _logoutState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        observeProfileInteractor.dispose()
        observeProfileCompletePercentage.dispose()
    }
}