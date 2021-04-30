package com.vinners.cube_vishwakarma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
import com.vinners.cube_vishwakarma.feature_auth.ui.ObserveProfileInteractor
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileEvents {

    val logoutState: LiveData<Lse>

    val profile: LiveData<Profile>
    fun initViewModel()

}
class MainActivityViewModel@Inject constructor(
        private val userProfileRepository: ProfileRepository,
        private val observeProfileInteractor: ObserveProfileInteractor
) : ViewModel(), ProfileEvents  {

    private val _logoutState = MutableLiveData<Lse>()
    override val logoutState: LiveData<Lse> = _logoutState


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

    override fun initViewModel() {
        startObservingProfile()
    }
    private val _profile = MutableLiveData<Profile>()
    override val profile: LiveData<Profile> = _profile

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
}