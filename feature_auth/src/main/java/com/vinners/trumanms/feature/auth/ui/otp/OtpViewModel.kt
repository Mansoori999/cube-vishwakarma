package com.vinners.trumanms.feature.auth.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.iid.FirebaseInstanceId
import com.vinners.core.logger.Logger
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.SingleLiveEvent
import com.vinners.trumanms.core.mdm.DeviceInfoProvider
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.auth.LoginResponse
import com.vinners.trumanms.data.models.mdm.DeviceInformation
import com.vinners.trumanms.data.models.mdm.MobileInformation
import com.vinners.trumanms.data.models.otp.VerifyOtpLoginRequest
import com.vinners.trumanms.data.models.otp.VerifyOtpRequest
import com.vinners.trumanms.data.repository.AuthRepository
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.BuildConfig
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface OtpConfirmEvents {
    fun registerSmsReceiver(): LiveData<String>

    fun unregisterSmsReceiver(): LiveData<String>

    fun otpConfirmState(): LiveData<Lce<LoginResponse>>

    fun otpLoginConfirmState(): LiveData<Lce<LoginResponse>>

    fun resendOtpState(): LiveData<Lse>
}

class OtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager,
    private val logger: Logger,
    private val appInfo: AppInfo,
    private val deviceInfoProvider: DeviceInfoProvider
) : ViewModel(), OtpConfirmEvents {

    private var _isReadingOtp = true
    val isReadingOtp: Boolean get() = _isReadingOtp

    private val _registerSmsReceiver = MutableLiveData<String>()
    override fun registerSmsReceiver(): LiveData<String> = _registerSmsReceiver

    private val _unRegisterSmsReceiver = MutableLiveData<String>()
    override fun unregisterSmsReceiver(): LiveData<String> = _unRegisterSmsReceiver

    private val _otpConfirmState = MutableLiveData<Lce<LoginResponse>>()
    override fun otpConfirmState(): LiveData<Lce<LoginResponse>> = _otpConfirmState

    private val _otpLoginConfirmState = MutableLiveData<Lce<LoginResponse>>()
    override fun otpLoginConfirmState(): LiveData<Lce<LoginResponse>> = _otpLoginConfirmState

    private val _resendOtpState = MutableLiveData<Lse>()
    override fun resendOtpState(): LiveData<Lse> = _resendOtpState

    fun otpConfirm(otpToken: String, otp: String,referalCode: String?,gps: String?,address: String?) {
        _otpConfirmState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logger.d("Otp confirmed for register")
                _isReadingOtp = false
                _unRegisterSmsReceiver.postValue("Unregistering Sms receiver...")
               val response = authRepository.verifyOtp(VerifyOtpRequest(
                   otpToken = otpToken,
                   otp = otp,
                   referalCode = referalCode,
                   mobileInfo = getMobileInformation(),
                   gpsAddress = address,
                   gps = gps
               ))
                _otpConfirmState.postValue(Lce.content(response))
            } catch (e: Exception) {
                _isReadingOtp = true
                logger.d("registering Sms receiver")
                _registerSmsReceiver.postValue("Registering Sms receiver...")
                _otpConfirmState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun otpLoginConfirm(otpToken: String?, otp: String?,gps: String?,address: String?){
        _otpLoginConfirmState.value = Lce.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                _unRegisterSmsReceiver.postValue("Unregistering Sms receiver...")
                val response = authRepository.verifyOtpLogin(
                    VerifyOtpLoginRequest(
                    otpToken = otpToken,
                    otp = otp,
                    mobileInfo = getMobileInformation(),
                    gps = gps,
                    gpsAddress = address))
                _otpLoginConfirmState.postValue(Lce.Content(response))
                logger.d("Otp confirmed for login")
            }catch (e: Exception){
                _registerSmsReceiver.postValue("Registering Sms receiver...")
                _otpLoginConfirmState.postValue(Lce.Error(e.localizedMessage))
            }
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
            deviceInfo.appVersion = appInfo.appVersion
            return deviceInfo
        }
    }

    fun resendOtp(otpToken: String){
        viewModelScope.launch {
            _resendOtpState.value = Lse.Loading
            try {
                authRepository.resendOtp(otpToken)
                _resendOtpState.postValue(Lse.Success)
            }catch (e: Exception){
                _resendOtpState.postValue(Lse.Error(e.localizedMessage))
            }
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
}