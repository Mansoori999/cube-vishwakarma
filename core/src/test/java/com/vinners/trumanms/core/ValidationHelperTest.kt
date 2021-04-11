package com.vinners.trumanms.core

import org.junit.Assert.*
import org.junit.Test

class ValidationHelperTest {

    @Test
    fun testPhoneNumberValidations() {
        var phoneNumber = "9099960999"
        assertTrue(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "909996099"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "5099960999"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "90999d0999"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "text"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "9text"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

        phoneNumber = "9435"
        assertFalse(ValidationHelper.isValidPhoneNumber(phoneNumber))

    }

    @Test
    fun testEmailValidations() {
        var email = "user.name@domain.com"
        assertTrue(ValidationHelper.isEmailValid(email))

        email = "user.name@domain.verylongEnd"
        assertTrue(ValidationHelper.isEmailValid(email))

        email = "user.name@domain.co.in"
        assertTrue(ValidationHelper.isEmailValid(email))

        email = "user.name"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "user.name@domain"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "user.name@domain."
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "234324dd"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "user@domain.com@ddd.com"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "user@domain .com@ddd.com"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "      "
        assertFalse(ValidationHelper.isEmailValid(email))

        email = ""
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "testssss@"
        assertFalse(ValidationHelper.isEmailValid(email))

        email = "@testssss"
        assertFalse(ValidationHelper.isEmailValid(email))
    }
}