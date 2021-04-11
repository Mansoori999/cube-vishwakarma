package com.vinners.trumanms.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinners.trumanms.cache.dao.ProfileDao
import com.vinners.trumanms.cache.entities.CachedProfile
import com.vinners.trumanms.cache.typeConverter.DateTypeConverter

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
        const val DATABASE_NAME = "truman_7.db"
    }

    // The associated DAOs for the database
    abstract fun profileDao(): ProfileDao
}
