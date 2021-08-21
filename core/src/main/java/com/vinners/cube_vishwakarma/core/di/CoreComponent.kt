package com.vinners.cube_vishwakarma.core.di

import android.app.Application
import android.content.SharedPreferences
import android.location.Geocoder
import com.android.installreferrer.api.InstallReferrerClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.vinners.core.logger.Logger
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.AppSignatureHelper
import com.vinners.cube_vishwakarma.core.analytics.AnalyticsHelper
import com.vinners.cube_vishwakarma.core.di.modules.*
import com.vinners.cube_vishwakarma.core.executors.PostExecutionThread
import com.vinners.cube_vishwakarma.core.executors.ThreadExecutor
import com.vinners.cube_vishwakarma.core.locale.LocalisationRepository
import com.vinners.cube_vishwakarma.core.logger.LoggerImpl
import com.vinners.cube_vishwakarma.core.mdm.DeviceInfoProvider
import com.vinners.cube_vishwakarma.data.repository.*
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
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

    fun getComplaintsRepository(): MyComplaintRepository

    fun getOutletRepository(): OutletRepository

    fun getComplaintRequestRepository(): ComplaintRequestRepository

    fun getDashBoardRepository():DashBoardRepository

    fun getGeocode(): Geocoder

    fun getTutorialsRepository():TutorialsRepository

    fun getDocumentsRepository():DocumentsRepository

    fun getNearByRepository():NearByRepository

}