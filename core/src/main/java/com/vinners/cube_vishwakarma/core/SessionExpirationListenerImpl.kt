package com.vinners.cube_vishwakarma.core

import android.content.Context
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.remote.networkInterceptors.SessionExpirationListener

class SessionExpirationListenerImpl constructor(
    private val applicationContext: Context,
    private val userSessionManager: UserSessionManager
) : SessionExpirationListener {

    override fun sessionExpired() {

//        AppExecutors
//            .getInstance()
//            .mainThread()
//            .execute {
//
//                Toast.makeText(
//                    applicationContext,
//                    "Session Expired, Please Login Again",
//                    Toast.LENGTH_LONG
//                ).show()
//                userSessionManager.logoutUser()
//
//                // After logout redirect user to Login Activity
//                val i = Intent(applicationContext, LoginActivity::class.java)
//                // Closing all the Activities
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                // Add new Flag to start new Activity
//                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                // Staring Login Activity
//                applicationContext.startActivity(i)
//            }
    }
}