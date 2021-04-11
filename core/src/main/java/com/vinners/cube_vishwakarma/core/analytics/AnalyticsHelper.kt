package com.vinners.cube_vishwakarma.core.analytics

import android.os.Bundle

interface AnalyticsHelper {

    /**
     * Set User Identifier For Analytics
     * eg Email
     */
    fun setUser(userIdentifier: String)


    fun resetAnalyticsData()

    /**
     * Logs An Event In Anyalytics
     */
    fun logEvent(event: String, data: Bundle?)
}