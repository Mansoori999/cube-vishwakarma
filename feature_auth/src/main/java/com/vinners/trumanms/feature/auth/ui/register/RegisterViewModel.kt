package com.vinners.trumanms.feature.auth.ui.register

import androidx.lifecycle.*
import com.google.firebase.iid.FirebaseInstanceId
import com.vinners.core.logger.Logger
import com.vinners.trumanms.core.SingleLiveEvent
import com.vinners.trumanms.core.mdm.DeviceInfoProvider
import com.vinners.trumanms.feature.auth.ui.ObserveProfileInteractor
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.auth.RegisterRequest
import com.vinners.trumanms.data.models.mdm.MobileInformation
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.data.repository.AuthRepository
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.BuildConfig
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import com.vinners.trumanms.feature.auth.ui.register.validations.RegisterValidation
import com.vinners.trumanms.feature.auth.ui.register.validations.RegisterValidationException
import com.vinners.trumanms.feature.auth.ui.register.validations.RegisterValidationResult
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

sealed class ValidateState {
    data class ValidateFailed(val result: RegisterValidationResult) : ValidateState()

    object Success : ValidateState()
}

interface RegistrationEvents {
    val registerState: LiveData<Lse>

    val state: LiveData<Lce<List<State>>>

    val city: LiveData<Lce<List<City>>>

    val pincode: LiveData<Lce<List<Pincode>>>

    val individualBasicValidate: LiveData<ValidateState>

    val agencyBasicsValidate: LiveData<ValidateState>

    val cityValidate: LiveData<ValidateState>

    val pincodeValidate: LiveData<ValidateState>

    val profile : LiveData<Profile>

    fun initViewModel()
}

class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val observeProfileInteractor: ObserveProfileInteractor,
    private val userSessionManager: UserSessionManager,
    private val logger: Logger,
    private val deviceInfoProvider: DeviceInfoProvider
) : AuthViewModel(userSessionManager), RegistrationEvents {

    override fun initViewModel() {
        startObservingProfile()
    }

    private val _profile = MutableLiveData<Profile>()
    override val profile : LiveData<Profile> = _profile

    private val _registerState = SingleLiveEvent<Lse>()
    override val registerState: LiveData<Lse> = _registerState

    private val _state = SingleLiveEvent<Lce<List<State>>>()
    override val state: LiveData<Lce<List<State>>> = _state

    private val _city = SingleLiveEvent<Lce<List<City>>>()
    override val city: LiveData<Lce<List<City>>> = _city

    private val _pincode = SingleLiveEvent<Lce<List<Pincode>>>()
    override val pincode: LiveData<Lce<List<Pincode>>> = _pincode


    private fun startObservingProfile(){
        observeProfileInteractor.execute(GetProfileObserver(), null)
    }

    private inner class GetProfileObserver : DisposableObserver<Profile>() {

        override fun onComplete() {}

        override fun onError(e: Throwable) {}

        override fun onNext(t: Profile) {
            _profile.postValue(t)
        }
    }

    fun register(registerRequest: RegisterRequest) {
        _registerState.value = Lse.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerRequest.mobileInfo = getMobileInformation()
                authRepository.register(registerRequest)
                _registerState.postValue(Lse.success())
            } catch (e: Exception) {
                _registerState.postValue(Lse.Error(e.localizedMessage))
            }
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

    fun getCity(stateId: String?) {
        _city.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.getCity(stateId)
                _city.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _city.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getPincode(districtId: String?) {
        _pincode.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.getPincode(districtId)
                _pincode.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _pincode.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    //Individual Basics validation fields

    private val _individualBasicsValidate = SingleLiveEvent<ValidateState>()
    override val individualBasicValidate: LiveData<ValidateState> = _individualBasicsValidate

    fun validateIndvidualBasics(firstName: String,email: String,dateOfBirth: Date?) {
        try {
            RegisterValidation.validateIndividualbasics(firstName,email,dateOfBirth)
            _individualBasicsValidate.postValue(ValidateState.Success)
        } catch (e: RegisterValidationException) {
            _individualBasicsValidate.postValue(ValidateState.ValidateFailed(e.result))
        }
    }

    //Agency Basics Validation Fields

    private val _agencyBasicsValidate = SingleLiveEvent<ValidateState>()
    override val agencyBasicsValidate: LiveData<ValidateState> = _agencyBasicsValidate

    fun validateAgencyBasics(agencyName: String,firstName: String,email: String){
        try {
            RegisterValidation.validateAgencyBasics(agencyName,firstName,email)
            _agencyBasicsValidate.postValue(ValidateState.Success)
        }catch (e: RegisterValidationException){
            _agencyBasicsValidate.postValue(ValidateState.ValidateFailed(e.result))
        }
    }

    private val _pincodeValidate = MutableLiveData<ValidateState>()
    override val pincodeValidate: LiveData<ValidateState> = _pincodeValidate

    fun valdatePincode(pincodeId: String?){
        try {
            RegisterValidation.validatePincode(pincodeId)
            _pincodeValidate.postValue(ValidateState.Success)
        }catch (e:RegisterValidationException){
            _pincodeValidate.postValue(ValidateState.ValidateFailed(e.result))
        }

    }

    private val _cityValidate = MutableLiveData<ValidateState>()
    override val cityValidate: LiveData<ValidateState> = _cityValidate

    fun valdateCity(cityId: String?){
        try {
            RegisterValidation.validateCity(cityId)
            _cityValidate.postValue(ValidateState.Success)
        }catch (e:RegisterValidationException){
            _pincodeValidate.postValue(ValidateState.ValidateFailed(e.result))
        }

    }
    private suspend fun getMobileInformation(): MobileInformation {
        val deviceInfo = try {
            deviceInfoProvider.getDeviceInfo()
        } catch (e: Exception) {
            logger.e(e, "Error While Retreiving Mobile Information")
            null
        }

        if (deviceInfo == null) {
            return MobileInformation(
                deviceInformation = null,
                networkInfo = null,
                osInformation = null,
                deviceId = getFirebaseToken()
            )
        } else {
            deviceInfo.deviceId = getFirebaseToken()
            deviceInfo.appVersion = BuildConfig.VERSION_NAME
            return deviceInfo
        }
    }


    private suspend fun getFirebaseToken(): String? {
        return suspendCoroutine { contination ->
            FirebaseInstanceId
                .getInstance()
                .instanceId
                .addOnSuccessListener {
                    contination.resume(it.token)
                }.addOnFailureListener {
                    logger.e(it, "Error While Fetching Firebase Token")
                    contination.resume(null)
                }
        }
    }
    override fun onCleared() {
        super.onCleared()
        observeProfileInteractor.dispose()
    }
}
