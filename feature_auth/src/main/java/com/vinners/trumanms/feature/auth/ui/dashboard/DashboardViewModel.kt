package com.vinners.trumanms.feature.auth.ui.dashboard

import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.ui.AuthViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val userSessionManager: UserSessionManager
) : AuthViewModel(userSessionManager){
}