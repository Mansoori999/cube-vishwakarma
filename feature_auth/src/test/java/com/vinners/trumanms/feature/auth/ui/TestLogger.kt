package com.vinners.trumanms.feature.auth.ui

import android.util.Log
import com.vinners.core.logger.Logger

class TestLogger : Logger {

    override fun d(message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun d(tag: String, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun d(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun d(t: Throwable, message: String, vararg args: Any) {
        Log.e("TestLogger", message, t)
    }

    override fun e(message: String, vararg args: Any) {
        Log.e("TestLogger", message)
    }

    override fun e(t: Throwable) {
        t.printStackTrace()
    }

    override fun e(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eAndRethrow(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unBindUserFromLogger() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun v(message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun v(t: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun v(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun w(message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wtf(t: Throwable, message: String, vararg args: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}