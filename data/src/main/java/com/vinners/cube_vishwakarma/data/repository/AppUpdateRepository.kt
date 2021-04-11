package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.cube_vishwakarma.data.exceptions.AppVersionDiscontinuedException
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