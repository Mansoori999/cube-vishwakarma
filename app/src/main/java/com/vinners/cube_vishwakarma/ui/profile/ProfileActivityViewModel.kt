package com.vinners.cube_vishwakarma.ui.profile

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vinners.cube_vishwakarma.core.taskState.Lse
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.ProfileRepository
import com.vinners.cube_vishwakarma.feature_auth.ui.ObserveProfileInteractor
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

interface ProfileEvents {

    val profile: LiveData<Profile>
    fun initViewModel()
}
class ProfileActivityViewModel@Inject constructor(
        private val userProfileRepository: ProfileRepository,
        private val observeProfileInteractor: ObserveProfileInteractor
        ) : ViewModel(), ProfileEvents{

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