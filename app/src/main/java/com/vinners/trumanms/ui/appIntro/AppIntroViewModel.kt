package com.vinners.trumanms.ui.appIntro

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.vinners.trumanms.core.SingleLiveEvent
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import com.vinners.trumanms.ui.splash.SplashViewModel
import javax.inject.Inject
import javax.inject.Named

enum class AppIntroEvents {
    APP_INTRO_SHOWN,
    APP_INTRO_SKIPPED;
}

class AppIntroViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager,
    @Named("session_independent_pref") private val sharedPreferences: SharedPreferences
) : AuthViewModel(userSessionManager) {

    //TODO add analytics later

    private val _events: SingleLiveEvent<AppIntroEvents> = SingleLiveEvent()
    val events: LiveData<AppIntroEvents> = _events

    fun setAppIntroAsShown() {
        markAppIntroAsShown()
        _events.value = AppIntroEvents.APP_INTRO_SHOWN
    }

    fun setAppIntroAsSkipped() {
        markAppIntroAsShown()
        _events.value = AppIntroEvents.APP_INTRO_SKIPPED
    }

    private fun markAppIntroAsShown() {
        sharedPreferences.edit().putBoolean(SplashViewModel.APP_INTRO_SHOWN, true).apply()
    }
}