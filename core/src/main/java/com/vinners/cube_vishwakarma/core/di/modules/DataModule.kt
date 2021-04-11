package com.vinners.cube_vishwakarma.core.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
abstract class DataModule {

    @Module
    companion object {

        @Singleton
        @Provides
        @Named("session_independent_pref")
        @JvmStatic
        fun provideSessionIndependentPreferences(context: Context): SharedPreferences {
            // Returns a [SharedPreferences] which will be not be cleared when user logs out,
            // great for storing things like language selected, app intro shown etc
            return context.getSharedPreferences("session_independent_pref", Context.MODE_PRIVATE)
        }

        @Singleton
        @Provides
        @Named("session_dependent_pref")
        @JvmStatic
        fun provideSessionDependentPreferences(context: Context): SharedPreferences {
            // Returns a [SharedPreferences] which will be cleared when user logs out,
            // great for storing things like Current users related data
            return context.getSharedPreferences("session_dependent_pref", Context.MODE_PRIVATE)
        }
    }
}