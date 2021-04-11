package com.vinners.trumanms.feature.auth.ui

import androidx.lifecycle.ViewModel
import com.vinners.trumanms.data.models.LoggedInUser
import com.vinners.trumanms.data.repository.AuthRepository
import com.vinners.trumanms.data.sessionManagement.UserSessionManager

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