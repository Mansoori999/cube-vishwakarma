package com.vinners.cube_vishwakarma.core.extensions

import java.util.*

/**
 * Checks date with the year specified
 *
 * @return true - if your date is older than given year
 * false - if your date is younger than given year
 */
fun Date.isDateOlderThanYear(year: Int): Boolean {

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    return calendar.time.after(this)
}

/**
 * Checks date with the year specified
 *
 * @return true - if your date is older than given year
 * false - if your date is younger than given year
 */
fun Date.isDateYoungerThanYear(year: Int): Boolean {

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    return calendar.time.before(this)
}