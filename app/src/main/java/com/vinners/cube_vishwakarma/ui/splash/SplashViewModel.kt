package com.vinners.cube_vishwakarma.ui.splash

import android.content.SharedPreferences
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.vinners.core.logger.Logger
import com.vinners.cube_vishwakarma.BuildConfig
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.SingleLiveEvent
import com.vinners.cube_vishwakarma.core.locale.LocalisationRepository
import com.vinners.cube_vishwakarma.core.taskState.Lce
import com.vinners.cube_vishwakarma.data.exceptions.AppVersionDiscontinuedException
import com.vinners.cube_vishwakarma.data.models.profile.AppVersion
import com.vinners.cube_vishwakarma.data.repository.AppUpdateRepository
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class LauncherActivityState {

    object AppVersionDiscontinued : LauncherActivityState()

    object LocaleNotSelected : LauncherActivityState()

    object UserNotLoggedIn : LauncherActivityState()

    object UserLoggedIn : LauncherActivityState()

    object ShowAppIntro : LauncherActivityState()
}

data class LoginData(val referCode: String?)

class SplashViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    private val localisationRepository: LocalisationRepository,
    @Named("session_independent_pref") private val sharedPreferences: SharedPreferences,
    private val appUpdateRepository: AppUpdateRepository,
    private val installReferrerClient: InstallReferrerClient,
    private val logger: Logger,
    private val appInfo: AppInfo
) : ViewModel(), InstallReferrerStateListener {

    private var appIntroShown: Boolean = false

    private val _launcherState = SingleLiveEvent<LauncherActivityState>()
    val launcherState: LiveData<LauncherActivityState> = _launcherState

    private val _loginState = MutableLiveData<Lce<LoginData>>()
    val loginState: LiveData<Lce<LoginData>> = _loginState

    private val _appIntroState = MutableLiveData<Lce<LoginData>>()
    val appIntroState: LiveData<Lce<LoginData>> = _appIntroState


    init {
        viewModelScope.launch {


            delay(4000L)
            _launcherState.postValue(LauncherActivityState.UserNotLoggedIn)
            return@launch

            logger.d("Init...")
            delay(SPLASH_TIME)

           if (appInfo.debugBuild) {
                checkForUserLogin()
            } else {
                logger.d("shouldCheckForAppUpdate() : true, checking for app update....")
                checkForAppUpdate()
            }
        }
    }

    private suspend fun checkForUserLogin() {
        if (false) {
            logger.d("isUserLoggedIn() : true")
            _launcherState.postValue(LauncherActivityState.UserLoggedIn)
        } else {
            logger.d("appIntroShown : true")
            appIntroShown = sharedPreferences.getBoolean(APP_INTRO_SHOWN, false)
           // checkForReferrer()
            checkIfNeedToShowAppIntro()
        }
    }

    private fun checkIfNeedToShowAppIntro() {

        if (appIntroShown) {
            logger.d("appIntroShown : true")
            _launcherState.postValue(LauncherActivityState.UserNotLoggedIn)
        } else
            _launcherState.postValue(LauncherActivityState.ShowAppIntro)
    }

    private fun shouldCheckForAppUpdate(): Boolean {
        val lastUpdateCheckedAt = sharedPreferences.getLong(LAST_APP_UPDATE_CHECK_TIME, -1)

        if (lastUpdateCheckedAt == -1L)
            return true

        val timeDiff = Date().time - lastUpdateCheckedAt
        val diffInHours = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS)
        return diffInHours >= FOUR_HOURS
    }

    private suspend fun checkForAppUpdate() = withTimeout(MAX_APP_UPDATE_CHECKING_TIME) {
        try {
            logger.d("Checking for app update.....")
            appUpdateRepository.checkForAppUpdate(appInfo.appVersion)

            logger.d("checkForAppUpdate: no updated available")
            sharedPreferences.edit().putLong(LAST_APP_UPDATE_CHECK_TIME, Date().time).apply()

            checkForUserLogin()
        } catch (e: AppVersionDiscontinuedException) {
            logger.d("checkForAppUpdate: App version discontinued [${appInfo.appVersion}]")
            _launcherState.postValue(LauncherActivityState.AppVersionDiscontinued)
        } catch (e: Exception) {
            logger.e(e, "checkForAppUpdate: Error while checking app update")
            checkForUserLogin()
        }
    }

    private suspend fun checkIfLanguageSelected() {

        logger.d("Checking if locale is set...")
        //if (localisationRepository.isLocaleSet()) {
        logger.d("Locale is set to ${localisationRepository.getCurrentLanguage().languageNameShort}")
        checkIfNeedToShowAppIntro()
        //} else {
        // logger.d("Locale is Not set.")
        // _launcherState.postValue(LauncherActivityState.LocaleNotSelected)
        //}
    }

    companion object {
        const val APP_INTRO_SHOWN = "app_intro_shown"
        private const val LAST_APP_UPDATE_CHECK_TIME = "last_app_update_check_time"
        private const val FOUR_HOURS = 4
        private const val MAX_APP_UPDATE_CHECKING_TIME = 20_000L
        private const val SPLASH_TIME = 100L
    }

    private fun checkForReferrer() {
        installReferrerClient.startConnection(this)
    }

    override fun onInstallReferrerSetupFinished(responseCode: Int) {
        when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {
                val response = installReferrerClient.installReferrer
                var referCode: String? = null
                //Currently hardcoding it
                try {
                    response.installReferrer?.split("&")?.forEach {
                        if (it.contains("utm_source")) {
                            referCode = if (it.trim().substring(11).isDigitsOnly())
                                it.trim().substring(11)
                            else
                                null
                        }
                    }
                } catch (e: Exception) {
                    logger.d("Refer failed",e.localizedMessage)
                }
                Log.d("ReferCode",referCode)
                if (appIntroShown)
                    _loginState.postValue(Lce.Content(LoginData(referCode)))
                else
                    _appIntroState.postValue(Lce.Content(LoginData(referCode)))
            }
            else -> {
                if (appIntroShown)
                    _loginState.postValue(Lce.Content(LoginData(null)))
                else
                    _appIntroState.postValue(Lce.Content(LoginData(null)))
                logger.d("Install Referrer Failure", "Code : $responseCode")
            }
        }
    }

    override fun onInstallReferrerServiceDisconnected() {

    }

    override fun onCleared() {
        super.onCleared()
        installReferrerClient.endConnection()
    }
}