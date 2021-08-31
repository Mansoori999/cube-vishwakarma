package com.vinners.cube_vishwakarma.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vinners.cube_vishwakarma.cache.dao.DashboardDao
import com.vinners.cube_vishwakarma.cache.dao.MyComplaintDao
import com.vinners.cube_vishwakarma.cache.dao.OutletsDao
import com.vinners.cube_vishwakarma.cache.typeConverter.DateTypeConverter
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.Converter
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem
import com.vinners.cube_vishwakarma.data.models.dashboard.MonthWiseConverter
import com.vinners.cube_vishwakarma.data.models.dashboard.ROWiseConverter
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

/**
 * Created by Himanshu on 6/22/2018.
 */

@Database(
    entities = [
        OutletsList::class,
        MyComplaintList::class,
        DashBoardResponseDataItem::class
    ],
    version = 20,
    exportSchema = false
)
@TypeConverters(
    DateTypeConverter::class,
    Converter::class,
    MonthWiseConverter::class,
    ROWiseConverter::class

)
abstract class OutletsLocalDatabase : RoomDatabase() {
    // The associated DAOs for the database
    abstract fun getOutletsDao(): OutletsDao

    abstract fun getMyComplaintDao(): MyComplaintDao

    abstract fun getDashboardDao(): DashboardDao

    companion object {
        const val DATABASE_NAME = "cube_outlets_and_complaints.db"

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
