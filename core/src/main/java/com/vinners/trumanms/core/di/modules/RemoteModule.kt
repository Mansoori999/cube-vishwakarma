package com.vinners.trumanms.core.di.modules

import android.content.Context
import com.vinners.core.logger.Logger
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.SessionExpirationListenerImpl
import com.vinners.trumanms.data.dataStores.AuthRemoteDataStore
import com.vinners.trumanms.data.dataStores.help.HelpRemoteDataStore
import com.vinners.trumanms.data.dataStores.jobs.JobsRemoteDataStore
import com.vinners.trumanms.data.dataStores.money.MoneyRemoteDataStore
import com.vinners.trumanms.data.dataStores.notification.NotificationRemoteDataStore
import com.vinners.trumanms.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.remote.RetrofitServiceFactory
import com.vinners.trumanms.remote.dataStoreImpl.*
import com.vinners.trumanms.remote.networkInterceptors.NetworkCallInterceptor
import com.vinners.trumanms.remote.retrofitServices.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Module that provides all dependencies from the repository package/layer.
 */
@Module
abstract class RemoteModule {

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideAuthService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): AuthService {
            return retrofitServiceFactory.prepareService(AuthService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideDashboardService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): JobsService {
            return retrofitServiceFactory.prepareService(JobsService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideProfileService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): ProfileService {
            return retrofitServiceFactory.prepareService(ProfileService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideHelpService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): HelpService {
            return retrofitServiceFactory.prepareService(HelpService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideMoneyService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): MoneyService {
            return retrofitServiceFactory.prepareService(MoneyService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideNotificationService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): NotificationService {
            return retrofitServiceFactory.prepareService(NotificationService::class.java)
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideRetrofitFactory(
            appInfo: AppInfo,
            networkCallInterceptor: NetworkCallInterceptor
        ): RetrofitServiceFactory {
            return RetrofitServiceFactory(
                appInfo,
                networkCallInterceptor
            )
        }

        @Provides
        @JvmStatic
        fun provideIfscService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): IfscService {
            return retrofitServiceFactory.prepareIfscService(IfscService::class.java)
        }


        @JvmStatic
        @Provides
        @Singleton
        fun provideNetworkCallInterceptor(
            appContext: Context,
            logger: Logger,
            appInfo: AppInfo,
            userSessionManager: UserSessionManager
        ): NetworkCallInterceptor {

            /** Headers Common for All Request*/
            val headers = mutableMapOf(
                "Source" to "android",
                "Version" to appInfo.appVersion,
                "LastCommit" to appInfo.lastCommit
            )

            userSessionManager.sessionToken?.let {
                headers.put("authToken", it)
            }

            return NetworkCallInterceptor(logger)
                .setDefaultHeaders(headers)
                .setSessionExpirationListener(
                    SessionExpirationListenerImpl(
                        appContext,
                        userSessionManager
                    )
                )
        }
    }

    @Binds
    abstract fun bindAuthRemoteDataSource(authRemoteDataStoreImpl: AuthRemoteDataStoreImpl): AuthRemoteDataStore

    @Binds
    abstract fun bindDashboardRemoteDataSource(dashboardRemotedataStoteImp: JobsRemotedataStoteImp): JobsRemoteDataStore

    @Binds
    abstract fun bindProfileRemoteDataSource(userProfileRemoteDataStoreImpl: UserProfileRemoteDataStoreImpl): UserProfileRemoteDataSource

    @Binds
    abstract fun bindHelpRemoteDataSource(helpRemoteDataStoreImpl: HelpRemoteDataStoreImpl): HelpRemoteDataStore

    @Binds
    abstract fun bindMoneyRemoteDataSource(moneyRemoteDataStoreImpl: MoneyRemoteDataStoreImpl): MoneyRemoteDataStore

    @Binds
    abstract fun bindNotificationRemoteDataSource(notificationRemoteDataStoreImp: NotificationRemoteDataStoreImp): NotificationRemoteDataStore
}