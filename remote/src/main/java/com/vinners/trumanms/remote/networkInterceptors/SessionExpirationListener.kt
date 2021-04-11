package com.vinners.trumanms.remote.networkInterceptors

import java.net.HttpURLConnection

interface SessionExpirationListener {

    /**
     * Whenever any API gets [HttpURLConnection.HTTP_UNAUTHORIZED]
     * as Response Code In Any API this function will be called
     */
    fun sessionExpired()
}