package com.vinners.cube_vishwakarma.core.di.modules

import android.app.Application
import androidx.room.Room
import com.vinners.cube_vishwakarma.cache.LocalDatabase
import com.vinners.cube_vishwakarma.cache.dao.ProfileDao
import com.vinners.cube_vishwakarma.cache.localDataStoreImpl.UserProfileLocalDataStoreImpl
import com.vinners.cube_vishwakarma.data.dataStores.userProfile.UserProfileLocalDataStore
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
        fun provideProfileDao(localDatabase: LocalDatabase): ProfileDao {
            return localDatabase.profileDao()
        }
    }

    @Singleton
    @Binds
    abstract fun bindProfileLocalStore(profileLocalDataStoreImpl: UserProfileLocalDataStoreImpl): UserProfileLocalDataStore

}