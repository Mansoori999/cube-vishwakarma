package com.vinners.cube_vishwakarma.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinners.cube_vishwakarma.cache.dao.ProfileDao
import com.vinners.cube_vishwakarma.cache.entities.CachedProfile
import com.vinners.cube_vishwakarma.cache.typeConverter.DateTypeConverter

/**
 * Created by Himanshu on 6/22/2018.
 */

@Database(
    entities = [
        CachedProfile::class
    ],
    version = 14,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class
)
abstract class LocalDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "cube_1.db"
    }

    // The associated DAOs for the database
    abstract fun profileDao(): ProfileDao
}
