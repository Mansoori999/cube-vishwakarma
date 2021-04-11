package com.vinners.trumanms.cache.typeConverter

import androidx.room.TypeConverter
import java.util.Date


class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long?): Date? {

        return if (value == null)
            return null
        else
            Date(value)
    }

    @TypeConverter
    fun toLong(value: Date?): Long? {
        return value?.time ?: return null
    }
}