package com.vinners.cube_vishwakarma.core.di.modules

import android.app.Application
import androidx.room.Room
import com.vinners.cube_vishwakarma.cache.LocalDatabase
import com.vinners.cube_vishwakarma.cache.OutletsLocalDatabase
import com.vinners.cube_vishwakarma.cache.dao.DashboardDao
import com.vinners.cube_vishwakarma.cache.dao.MyComplaintDao
import com.vinners.cube_vishwakarma.cache.dao.OutletsDao
import com.vinners.cube_vishwakarma.cache.dao.ProfileDao
import com.vinners.cube_vishwakarma.cache.localDataStoreImpl.OutletLocalDataStoreImpl
import com.vinners.cube_vishwakarma.cache.localDataStoreImpl.UserProfileLocalDataStoreImpl
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletsLocalDataStore
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
import com.vinners.cube_vishwakarma.data.repository.OutletRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class CacheModule {

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {

        @Singleton
        @Provides
        @JvmStatic
        fun provideLocalDatabase(application: Application): LocalDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                LocalDatabase::class.java,
                LocalDatabase.DATABASE_NAME
            ).build()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideOutletsLocalDatabase(application: Application): OutletsLocalDatabase {
            return Room.databaseBuilder(
                application.applicationContext,
                OutletsLocalDatabase::class.java,
                OutletsLocalDatabase.DATABASE_NAME
            ).build()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideProfileDao(localDatabase: LocalDatabase): ProfileDao {
            return localDatabase.profileDao()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideOutletsDao(outletsLocalDatabase: OutletsLocalDatabase): OutletsDao {
            return outletsLocalDatabase.getOutletsDao()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideMyComplaintDao(outletsLocalDatabase: OutletsLocalDatabase): MyComplaintDao {
            return outletsLocalDatabase.getMyComplaintDao()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideDashboardDao(outletsLocalDatabase: OutletsLocalDatabase): DashboardDao {
            return outletsLocalDatabase.getDashboardDao()
        }

    }

    @Singleton
    @Binds
    abstract fun bindProfileLocalStore(profileLocalDataStoreImpl: UserProfileLocalDataStoreImpl): UserProfileLocalDataStore

    @Singleton
    @Binds
    abstract fun bindOutletsLocalStore(outletLocalDataStoreImpl: OutletLocalDataStoreImpl): OutletsLocalDataStore


}