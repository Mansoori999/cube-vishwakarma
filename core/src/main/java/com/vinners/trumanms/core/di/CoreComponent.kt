package com.vinners.trumanms.core.di

import android.app.Application
import android.content.SharedPreferences
import com.android.installreferrer.api.InstallReferrerClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.vinners.core.logger.Logger
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.AppSignatureHelper
import com.vinners.trumanms.core.analytics.AnalyticsHelper
import com.vinners.trumanms.core.di.modules.*
import com.vinners.trumanms.core.executors.PostExecutionThread
import com.vinners.trumanms.core.executors.ThreadExecutor
import com.vinners.trumanms.core.locale.LocalisationRepository
import com.vinners.trumanms.core.logger.LoggerImpl
import com.vinners.trumanms.core.mdm.DeviceInfoProvider
import com.vinners.trumanms.data.repository.*
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import dagger.BindsInstance
import dagger.Component
import java.util.concurrent.ThreadPoolExecutor
import javax.inject.Named
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        DataModule::class,
        CoreModule::class,
        ContextModule::class,
        CacheModule::class,
        RemoteModule::class
    ]
)
interface CoreComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun appInfo(appInfo: AppInfo): Builder

        fun build(): CoreComponent
    }

    fun injectApplication(application: Application)

    fun getLoggerImpl(): LoggerImpl

    fun getLogger(): Logger

    fun getAppInfo() : AppInfo

    fun getAnalyticsHelper(): AnalyticsHelper

    fun getFirebaseAnalytics(): FirebaseAnalytics

    fun getUserSessionManager() : UserSessionManager

    fun getReferalClient(): InstallReferrerClient

    @Named("session_independent_pref")
    fun getSessionIndependentSharedPref() : SharedPreferences

    fun getLocalisationRepository(): LocalisationRepository

    fun getAppUpdateRepository() : AppUpdateRepository

    fun getProfileRepository(): ProfileRepository

    fun getAuthRepository() : AuthRepository

    fun getHelpRepositry(): HelpRepository

    fun getDashboardRepository(): JobsRepository

    fun getWalletRepository(): WalletRepository

    fun getNotificationRepository(): NotificationRepositry

    fun getAppSignature(): AppSignatureHelper

    fun getDeviceInfoProvider() : DeviceInfoProvider

    fun getThreadExecuter(): ThreadExecutor

    fun getPostThreadExecuter(): PostExecutionThread

}