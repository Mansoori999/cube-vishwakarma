package com.vinners.cube_vishwakarma

import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.base.CoreApplication
import java.time.format.DateTimeFormatter

val DEBUG_APP_INFO = AppInfo(
    debugBuild = true,
    appVersion = BuildConfig.VERSION_NAME,
    packageName = BuildConfig.APPLICATION_ID,
    lastCommit = BuildConfig.GIT_HASH,
    baseApiUrl = BuildConfig.API_URL
)

class TrumanApp : CoreApplication(
    appInfo = DEBUG_APP_INFO
) {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        Stetho.initializeWithDefaults(this)
    }
}