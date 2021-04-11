package com.vinners.trumanms.core.logger

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.vinners.core.logger.Logger
import com.vinners.trumanms.base.AppInfo
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerImpl @Inject constructor(
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val appInfo: AppInfo
) : Logger {

    fun init(debugMode: Boolean) {
        if (debugMode) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
            firebaseCrashlytics.setCustomKey("lastCommit", appInfo.lastCommit)
        }
    }

    override fun d(message: String, vararg args: Any) {
        try {
            Timber.d(message, args)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun d(tag: String, message: String, vararg args: Any) {
        Timber.d(tag, message, args)
    }

    override fun d(t: Throwable) {
        Timber.d(t)
    }

    override fun d(t: Throwable, message: String, vararg args: Any) {
        Timber.d(t)
    }

    override fun e(message: String, vararg args: Any) {
        Timber.e(message, args)
    }

    override fun e(t: Throwable) {
        Timber.e(t)
    }

    override fun e(t: Throwable, message: String, vararg args: Any) {
        Timber.e(t, message, args)
    }

    override fun eAndRethrow(t: Throwable, message: String, vararg args: Any) {
        e(t, message, args)
        throw t
    }

    override fun i(message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun i(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun i(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUserInfoForLogger(
        userIdentifier: String,
        userName: String,
        userEmail: String?
    ) {
        firebaseCrashlytics.setUserId(userIdentifier)
        firebaseCrashlytics.setCustomKey("name", userName)
        firebaseCrashlytics.setCustomKey("email", userIdentifier)
    }

    override fun unBindUserFromLogger() {}

    override fun v(message: String, vararg args: Any) {
        Timber.v(message, args)
    }

    override fun v(t: Throwable) {
        Timber.v(t)
    }

    override fun v(t: Throwable, message: String, vararg args: Any) {
        Timber.v(t, message, args)
    }

    override fun w(message: String, vararg args: Any) {
        Timber.w(message, args)
    }

    override fun w(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun w(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wtf(message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wtf(t: Throwable) {
        Timber.wtf(t)
    }

    override fun wtf(t: Throwable, message: String, vararg args: Any) {
        Timber.wtf(t, message, args)
    }
}