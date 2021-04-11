package com.vinners.cube_vishwakarma.core.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.vinners.core.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsHelperImpl @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val logger: Logger
) : AnalyticsHelper {

    override fun setUser(userIdentifier: String) {
        analytics.setUserId(userIdentifier)
    }

    override fun resetAnalyticsData() {
        analytics.resetAnalyticsData()
    }

    override fun logEvent(event: String, data: Bundle?) {
        try {
            analytics.logEvent(event, data)
        } catch (e: Exception) {
            if (data != null)
                logger.e(e, "unable to log event in analytics", data)
            else
                logger.e(e, "unable to log event in analytics")
        }

    }
}