package com.vinners.cube_vishwakarma.cache

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinners.cube_vishwakarma.cache.dao.OutletsDao
import com.vinners.cube_vishwakarma.cache.entities.CachedOutlets
import com.vinners.cube_vishwakarma.cache.typeConverter.DateTypeConverter
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

/**
 * Created by Himanshu on 6/22/2018.
 */

@Database(
    entities = [
        OutletsList::class
    ],
    version = 15,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class OutletsLocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract fun getOutletsDao(): OutletsDao

    companion object {
        const val DATABASE_NAME = "cube_outlets.db"

//        private var INSTANCE: OutletsLocalDatabase ?= null
//        fun getInstance(application: Application): OutletsLocalDatabase {
//            if (INSTANCE == null) {
//                INSTANCE = Room.databaseBuilder(
//                        application,
//                        OutletsLocalDatabase::class.java,
//                        DATABASE_NAME)
//                    .allowMainThreadQueries()
//                    .build()
//            }
//            return INSTANCE as OutletsLocalDatabase
//        }


    }

}
