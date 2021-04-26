package com.vinners.cube_vishwakarma.feature_auth.ui

import androidx.lifecycle.ViewModel
import com.vinners.cube_vishwakarma.data.models.LoggedInUser
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import kotlin.jvm.Throws

open class AuthViewModel(
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    suspend fun isUserLoggedIn(): Boolean {
        return userSessionManager.getLoggedInUser() != null
    }

    @Throws(IllegalStateException::class)
    suspend fun getLoggedInUser(): LoggedInUser {
        return userSessionManager.getLoggedInUser()
            ?: throw IllegalStateException("getLoggedInUser() , no logged In User")
    }
}