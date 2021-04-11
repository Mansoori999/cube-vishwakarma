package com.vinners.cube_vishwakarma.core.di.modules

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.android.installreferrer.api.InstallReferrerClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.vinners.core.logger.Logger
import com.vinners.cube_vishwakarma.core.analytics.AnalyticsHelper
import com.vinners.cube_vishwakarma.core.analytics.AnalyticsHelperImpl
import com.vinners.cube_vishwakarma.core.executors.PostExecutionThread
import com.vinners.cube_vishwakarma.core.executors.ThreadExecutor
import com.vinners.cube_vishwakarma.core.executors.ThreadExecutorImpl
import com.vinners.cube_vishwakarma.core.executors.UiThread
import com.vinners.cube_vishwakarma.core.logger.LoggerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class CoreModule {

    @Module
    companion object {

        @SuppressLint("MissingPermission")
        @Singleton
        @Provides
        @JvmStatic
        fun provideFirebaseAnalytics(context: Context): FirebaseAnalytics {
            return FirebaseAnalytics.getInstance(context)
        }

        @SuppressLint("MissingPermission")
        @Singleton
        @Provides
        @JvmStatic
        fun provideFirebaseCrashlytics(context: Context): FirebaseCrashlytics {
            return FirebaseCrashlytics.getInstance()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun providesThreadExecutor(): ThreadExecutor {
            return ThreadExecutorImpl()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun providesInstallReferrerClient(context: Context): InstallReferrerClient {
            return InstallReferrerClient.newBuilder(context).build()
        }
    }
    @Singleton
    @Binds
    abstract fun bindPostExecutionThread(uiThread: UiThread): PostExecutionThread


    @Singleton
    @Binds
    abstract fun bindAnalyticsHelper(analyticsHelperImpl: AnalyticsHelperImpl): AnalyticsHelper

    @Singleton
    @Binds
    abstract fun bindLogger(loggerImpl: LoggerImpl): Logger


}