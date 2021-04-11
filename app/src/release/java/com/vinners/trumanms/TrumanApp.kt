package com.vinners.trumanms

import androidx.multidex.MultiDex
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.CoreApplication

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
