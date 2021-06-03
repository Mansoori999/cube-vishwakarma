package com.vinners.cube_vishwakarma

import androidx.multidex.MultiDex
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.CoreApplication
import java.time.format.DateTimeFormatter

val RELEASE_APP_INFO = AppInfo(
    debugBuild = false,
    appVersion = BuildConfig.VERSION_NAME,
    packageName = BuildConfig.APPLICATION_ID,
    lastCommit = BuildConfig.GIT_HASH,
    baseApiUrl = BuildConfig.API_URL
)

class TrumanApp : CoreApplication(
    appInfo = RELEASE_APP_INFO
) {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}
