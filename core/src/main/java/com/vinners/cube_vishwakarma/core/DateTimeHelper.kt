package com.vinners.cube_vishwakarma.core

import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

object DateTimeHelper {

    private const val DATE_TIME_FORMAT_DDMMYYYY = "dd-MM-yyyy"
    private const val DATE_TIME_FORMAT_YYYYMMDD = "yyyy-MM-dd"
    private const val DATE_TIME_FORMAT_YYYYMMDDHHMMAA = "yyyy-MM-dd HH:mm:ss"
    private const val DATE_TIME_FORMAT_LONG = "dd LLL yyyy HH:mm:ss"
    private const val DATE_TIME_FORMAT_SHORT = "LLL dd, hh:mm aa"
    private const val DATE_TIME_SHORT_DATE = "LLL dd"
    private const val DATE_TIME_MONTH = "LLL"
    private const val DATE_TIME_DAY = "dd"
    private const val DATE_TIME_YEAR = "yyyy"
    private const val DATE_TIME_FANCY_DATE = "dd LLL yyyy"
    private const val DATE_TIME_FANCY_DATE_TIME = "dd LLL yyyy HH:mm:ss"

    private val todaysCalendar: Calendar by lazy { Calendar.getInstance() }

    private lateinit var dateFormatter: SimpleDateFormat

    /**
     * Return Current Day Of Month
     */
    val currentDayOfMonth: Int = todaysCalendar.get(Calendar.DAY_OF_MONTH)

    /**
     * Return Current Month Of the Year
     */
    val currenMonthOfYear: Int =
        todaysCalendar.get(Calendar.MONTH) + 1 // because in Calendar Month starts from 0

    /**
     * Return Current Year
     */
    val currentYear: Int = todaysCalendar.get(Calendar.YEAR)

    /**
     * Gets Todays Date in Short Format ex. Jun 06, 12:00 AM
     */
    fun getShortDateTimeInfo(): String {
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_SHORT, Locale.getDefault())
        return dateFormatter.format(todaysCalendar.time)
    }

    fun getShortDate(date: Date): String {
        dateFormatter = SimpleDateFormat(DATE_TIME_SHORT_DATE, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getFancyDate(date: Date): String {
        dateFormatter = SimpleDateFormat(DATE_TIME_FANCY_DATE, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getDDMMYYYYDate(date: Date): String {
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_DDMMYYYY, Locale.getDefault())
        return dateFormatter.format(date.time)
    }
    fun getYYYYMMDDDate(date: Date): String {
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getDDMMYYYYDateFromString(dateString: String): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_DDMMYYYY, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getYYYYMMDDDateFromString(dateString: String): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_DDMMYYYY, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getMonthFromString(dateString: String): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_MONTH, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getDayFromString(dateString: String): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_DAY, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getYearFromString(dateString: String): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_YEAR, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getFancyDateFromString(dateString: String?): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_FANCY_DATE, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    fun getFancyDateWithTimeFromString(dateString: String?): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDDHHMMAA).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_FANCY_DATE_TIME, Locale.getDefault())
        return dateFormatter.format(date.time)
    }


    fun getLongDateFromString(dateString: String?): String {
        val date = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDDHHMMAA).parse(dateString)
        dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_LONG, Locale.getDefault())
        return dateFormatter.format(date.time)
    }

    /**
     * Return Current Date in YYYY-MM-DD format ex 2018-06-04
     */
    val todaysDateInYYYYMMDD: String
        get() {
            dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD, Locale.getDefault())
            return dateFormatter.format(todaysCalendar.time)
        }

    /**
     * returns todays date in DD-MM-YYYY format ex. 04-06-2018
     */
    val todaysDateInDDMMYYY: String
        get() {
            dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_DDMMYYYY, Locale.getDefault())
            return dateFormatter.format(todaysCalendar.time)
        }

    fun getDateFromYYYYMMDDformate(dateString: String): Date{
        val dateFormatter = SimpleDateFormat(DATE_TIME_FORMAT_YYYYMMDD,Locale.getDefault())
        val date = dateFormatter.parse(dateString)
        return date
    }
}