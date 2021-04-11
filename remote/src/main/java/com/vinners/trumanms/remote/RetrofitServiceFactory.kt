package com.vinners.trumanms.remote


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.remote.adapters.GsonExclusionStrategy
import com.vinners.trumanms.remote.adapters.LowerCaseEnumTypeAdapter
import com.vinners.trumanms.remote.networkInterceptors.NetworkCallInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitServiceFactory @Inject constructor(
    private val appInfo: AppInfo,
    networkCallInterceptor: NetworkCallInterceptor
) {

    private val retrofit: Retrofit
    private val ifscRetrofit: Retrofit

    init {
        val moshi = makeMoshi()
        val okHttpClient = makeOkHttpClient(networkCallInterceptor)
        retrofit = makeRetrofit(
            appInfo.baseApiUrl,
            okHttpClient,
            moshi
        )
        ifscRetrofit =makeRetrofit(
            "https://ifsc.razorpay.com/",
            okHttpClient,
            moshi
        )
    }


    private fun makeRetrofit(baseUrl: String, okHttpClient: OkHttpClient, moshi: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(moshi))
            .build()
    }

    private fun makeOkHttpClient(
        networkCallInterceptor: NetworkCallInterceptor
    ): OkHttpClient {

        val okHttpBuilder = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(networkCallInterceptor)

        if (appInfo.debugBuild) {
            okHttpBuilder.addInterceptor(makeNetworkCallLoggingInterceptor())
        }

        return okHttpBuilder.build()
    }

    private fun makeMoshi(): Gson {
        return GsonBuilder()
            .setExclusionStrategies(GsonExclusionStrategy())
            .registerTypeAdapterFactory(LowerCaseEnumTypeAdapter())
            .setLenient()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()

    }

    private fun makeNetworkCallLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply { this.level = HttpLoggingInterceptor.Level.BODY }
    }

    fun <T> prepareService(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> prepareIfscService(service: Class<T>): T {
        return ifscRetrofit.create(service)
    }
}