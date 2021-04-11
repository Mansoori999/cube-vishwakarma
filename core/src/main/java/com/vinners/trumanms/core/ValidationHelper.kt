package com.vinners.trumanms.core

object ValidationHelper {

    private const val INDIAN_PHONE_NUMBER_REG_EX = "^[6-9]\\d{9}\$"

    private const val EMAIL_REG_EX = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,20})$")

    fun isValidPhoneNumber(phoneNumber: String) = Regex(INDIAN_PHONE_NUMBER_REG_EX).containsMatchIn(phoneNumber)

    fun isEmailValid(email: String) = Regex(EMAIL_REG_EX).containsMatchIn(email)
}