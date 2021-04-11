package com.vinners.trumanms.remote.networkInterceptors

import com.vinners.core.logger.Logger
import com.vinners.trumanms.remote.exceptions.ErrorConnectingServerException
import com.vinners.trumanms.remote.exceptions.InternalServerErrorException
import com.vinners.trumanms.remote.exceptions.SessionExpiredException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection


class NetworkCallInterceptor constructor(
    private val logger: Logger
) : Interceptor {

    private var headers: Map<String, String?> = emptyMap()
    private var queryParams: Map<String, String> = emptyMap()
    private var sessionExpirationListener: SessionExpirationListener? = null

    fun setDefaultHeaders(headers: Map<String, String?>): NetworkCallInterceptor {
        this.headers = headers
        return this
    }

    fun setDefaultQueryParams(queryParams: Map<String, String>): NetworkCallInterceptor {
        this.queryParams = queryParams
        return this
    }

    fun setSessionExpirationListener(sessionExpirationListener: SessionExpirationListener): NetworkCallInterceptor {
        this.sessionExpirationListener = sessionExpirationListener
        return this
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        // Modifying Our Original request
        // Adding Required headers and query params to all requests
        val request = buildRequest(chain.request())

        // Spying on the call and Changing Error Message
        try {
            val response = chain.proceed(request)

            when (response.code) {
                500 -> throw InternalServerErrorException(response.message)
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    sessionExpirationListener?.sessionExpired()
                    throw SessionExpiredException(
                        "Session Expired , Please Login Again"
                    )
                }
                else -> return response
            }
        } catch (exception: Exception) {

            when (exception) {
                is IOException -> {
                    //It Was an Internet or Server Connection Issue ,not need to log it
                    throw IOException(
                        "Unable To Reach Server, Check Internet Connection",
                        ErrorConnectingServerException("No Internet Or Server Issue")
                    )
                }

                // An Ugly Hack to remove *HTTP 500* text from exception message
                // why to [IOException] -> because it does not break the app
                is InternalServerErrorException -> throw IOException(exception.message, exception)
                is SessionExpiredException -> throw IOException(exception.message)
                else -> {
                    //Some Serious Issue occurred, Logging it
                    logger.e(exception, "Some Serious Error While Networking")
                    throw exception
                }
            }
        }
    }


    private fun buildRequest(originalRequest: Request): Request {

        // Original Request Object
        val requestBuilder = originalRequest.newBuilder()

        //Adding Headers
        for (key in headers.keys) {

            if (headers[key] != null)
                requestBuilder.addHeader(key, headers[key]!!)
            else throw IllegalArgumentException("Illegal argument ${headers[key]} for ${key}")

        }


        //Adding Query Params
        if (queryParams.isNotEmpty()) {
            val originalHttpUrl = originalRequest.url

            val urlBuilder = originalHttpUrl.newBuilder()
            for (key in queryParams.keys) {

                if (queryParams[key] != null) {
                    urlBuilder.addQueryParameter(key, queryParams[key])
                }
            }
            requestBuilder.url(urlBuilder.build())
        }

        return requestBuilder.build()
    }


}

