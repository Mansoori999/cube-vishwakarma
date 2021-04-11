package com.vinners.trumanms.data.repository

import com.vinners.trumanms.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.trumanms.data.exceptions.AppVersionDiscontinuedException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUpdateRepository @Inject constructor(
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource
) {

    suspend fun checkForAppUpdate(appVersion: String) {
        val response = userProfileRemoteDataSource.getAppVersion(appVersion)
        if (response.isAllowed.not())
            throw AppVersionDiscontinuedException()
    }
}