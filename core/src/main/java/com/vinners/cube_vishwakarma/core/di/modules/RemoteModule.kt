package com.vinners.cube_vishwakarma.core.di.modules

import android.content.Context
import android.location.Geocoder
import com.vinners.core.logger.Logger
import com.vinners.cube_vishwakarma.base.AppInfo
import com.vinners.cube_vishwakarma.core.SessionExpirationListenerImpl
import com.vinners.cube_vishwakarma.data.dataStores.AuthRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.complaint.ComplaintRequestRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.dashboard.DashBoardRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.help.HelpRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.jobs.JobsRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.money.MoneyRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.notification.NotificationRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.tutorials.TutorialsRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileRemoteDataSource
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.remote.RetrofitServiceFactory
import com.vinners.cube_vishwakarma.remote.dataStoreImpl.*
import com.vinners.cube_vishwakarma.remote.networkInterceptors.NetworkCallInterceptor
import com.vinners.cube_vishwakarma.remote.retrofitServices.*
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
        fun provideJobsService(
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


        @Provides
        @JvmStatic
        fun provideMyComplaintService(
                retrofitServiceFactory: RetrofitServiceFactory
        ): MyComplaintService {
            return retrofitServiceFactory.prepareService(MyComplaintService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideOutletService(
                retrofitServiceFactory: RetrofitServiceFactory
        ): OutletService {
            return retrofitServiceFactory.prepareService(OutletService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideComplaintRequestService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): ComplaintRequestService {
            return retrofitServiceFactory.prepareService(ComplaintRequestService::class.java)
        }

        @Provides
        @JvmStatic
        fun provideDashBoardService(
            retrofitServiceFactory: RetrofitServiceFactory
        ): DashBoardService {
            return retrofitServiceFactory.prepareService(DashBoardService::class.java)
        }


        @Provides
        @JvmStatic
        fun provideTutorialsService(
                retrofitServiceFactory: RetrofitServiceFactory
        ): TutorialsService {
            return retrofitServiceFactory.prepareService(TutorialsService::class.java)
        }

    }

    @Binds
    abstract fun bindAuthRemoteDataSource(authRemoteDataStoreImpl: AuthRemoteDataStoreImpl): AuthRemoteDataStore

    @Binds
    abstract fun bindDashboardRemoteDataSource(jobsRemotedataStoteImp: JobsRemotedataStoteImp): JobsRemoteDataStore

    @Binds
    abstract fun bindProfileRemoteDataSource(userProfileRemoteDataStoreImpl: UserProfileRemoteDataStoreImpl): UserProfileRemoteDataSource

    @Binds
    abstract fun bindHelpRemoteDataSource(helpRemoteDataStoreImpl: HelpRemoteDataStoreImpl): HelpRemoteDataStore

    @Binds
    abstract fun bindMoneyRemoteDataSource(moneyRemoteDataStoreImpl: MoneyRemoteDataStoreImpl): MoneyRemoteDataStore

    @Binds
    abstract fun bindNotificationRemoteDataSource(notificationRemoteDataStoreImp: NotificationRemoteDataStoreImp): NotificationRemoteDataStore

    @Binds
    abstract fun bindMyComplaintRemoteDataSource(myComplaintRemoteDataStoreImpl: MyComplaintRemoteDataStoreImpl): MyComplaintRemoteDataStore

    @Binds
    abstract fun bindOutletRemoteDataStore(outletRemoteDataStoreImpl: OutletRemoteDataStoreImpl): OutletRemoteDataStore

    @Binds
    abstract fun bindComplaintRequestRemoteDataStore(complaintRequestRemoteDataStoreImpl: ComplaintRequestRemoteDataStoreImpl): ComplaintRequestRemoteDataStore

    @Binds
    abstract fun bindDashBoardRemoteDataStore(dashBoardRemoteDataStoreImpl: DashBoardRemoteDataStoreImpl): DashBoardRemoteDataStore

    @Binds
    abstract fun bindTutorialsRemoteDataStore(tutorialsRemoteDataStoreImpl: TutorialsRemoteDataStoreImpl): TutorialsRemoteDataStore


}