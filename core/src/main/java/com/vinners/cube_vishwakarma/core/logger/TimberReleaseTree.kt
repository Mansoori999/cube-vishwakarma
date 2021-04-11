package com.vinners.cube_vishwakarma.core.logger


import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class TimberReleaseTree : Timber.Tree() {

    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        crashlytics.log(message)
        if (t != null) crashlytics.recordException(t)
    }

}