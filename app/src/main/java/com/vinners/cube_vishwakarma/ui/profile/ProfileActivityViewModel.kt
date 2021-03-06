package com.vinners.cube_vishwakarma.ui.profile

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.auth.LoginResponse
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
import com.vinners.cube_vishwakarma.feature_auth.ui.ObserveProfileInteractor
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ProfileEvents {

    val profile: LiveData<Profile>
    val changeduserpasswordState: LiveData<Lce<String>>
    val refreshProfileState: LiveData<Lce<LoginResponse>>

    val profilePicState: LiveData<Lse>
    fun initViewModel()
    val percentageProfileCompleteState: LiveData<Int>
}
class ProfileActivityViewModel@Inject constructor(
        private val userProfileRepository: ProfileRepository,
        private val observeProfileInteractor: ObserveProfileInteractor,
        private val observeProfileCompletePercentage: ObserveProfileCompletePercentage
        ) : ViewModel(), ProfileEvents{

    override fun initViewModel() {
        startObservingProfile()

    }
    private val _profile = MutableLiveData<Profile>()
    override val profile: LiveData<Profile> = _profile

    private val _profilePercentageComplete = MutableLiveData<Int>()
    override val percentageProfileCompleteState: LiveData<Int> = _profilePercentageComplete


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


    private val _profilePicState = MutableLiveData<Lse>()
    override val profilePicState: LiveData<Lse> = _profilePicState

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

    private val _changeduserpasswordState = MutableLiveData<Lce<String>>()
    override val changeduserpasswordState: LiveData<Lce<String>> = _changeduserpasswordState

    fun changedUserPassword(newpassword : String) {
        _changeduserpasswordState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userProfileRepository.changedUserPassword(newpassword)
                _changeduserpasswordState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _changeduserpasswordState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }

    private val _refreshProfileState = MutableLiveData<Lce<LoginResponse>>()
    override val refreshProfileState: LiveData<Lce<LoginResponse>> = _refreshProfileState

    fun refreshProfileData() {
        _refreshProfileState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userProfileRepository.refreshProfileData()
                _refreshProfileState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _refreshProfileState.postValue(Lce.error(e.localizedMessage))

            }
        }
    }
}