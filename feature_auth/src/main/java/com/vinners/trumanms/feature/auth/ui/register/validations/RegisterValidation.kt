package com.vinners.trumanms.feature.auth.ui.register.validations

import android.content.Context
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.ValidationHelper
import com.vinners.trumanms.core.extensions.isDateOlderThanYear
import java.util.*
import javax.inject.Inject

object RegisterValidation {
    val registerValidationResult = RegisterValidationResult()

    fun validateIndividualbasics(
        firstName: String,
        email: String,
        dateOfBirth: Date?
    ): Boolean {
        validateIndividualName(firstName)
        validateIndidualEmail(email)
        validateDateOfBirth(dateOfBirth)
        return true
    }

    fun validateIndividualEducation(
        education: String,
        experience: String
    ): Boolean {
        validateEducation(education)
        return true
    }

    fun validateAgencyBasics(
        agencyName: String,
        contactPersonFirstName: String,
        email: String
    ): Boolean {
        validateAgencyName(agencyName)
        validateAgencyFirstName(contactPersonFirstName)
        validateAgencyEmail(email)
        return true
    }

    fun validateAgencyWork(
        contactPersonFirstName: String,
        contactPersonLastName: String
    ): Boolean {

        return true
    }

    fun validateLocation(
        pincode: String?
    ): Boolean {
        validatePincode(pincode)
        return true
    }

    private fun validateIndividualName(firstName: String) {
        registerValidationResult.firstNameValid = true
        if (firstName.isBlank()) {
            registerValidationResult.firstNameValid = false
            registerValidationResult.message = "First Name can not be empty"
            throw RegisterValidationException(registerValidationResult)
        } else if (firstName.length < 3) {
            registerValidationResult.firstNameValid = false
            registerValidationResult.message = "First Name is too short"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateIndividualLastName(lastName: String) {
        registerValidationResult.lastNameValid = true
        if (lastName.isBlank()) {
            registerValidationResult.lastNameValid = false
            registerValidationResult.message = "Last Name can not be empty"
            throw RegisterValidationException(registerValidationResult)
        } else if (lastName.length < 3) {
            registerValidationResult.lastNameValid = false
            registerValidationResult.message = "Last Name is too short"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateIndidualEmail(email: String) {
        if (email.isBlank())
            registerValidationResult.emailValid = true
        else if (!ValidationHelper.isEmailValid(email)) {
            registerValidationResult.emailValid = false
            registerValidationResult.message = "Invalid email entered"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateAgencyLastName(lastName: String) {
        registerValidationResult.contactPersonLastName = true
        if (lastName.isBlank()) {
            registerValidationResult.contactPersonLastName = false
            registerValidationResult.message = "Last Name can not be empty"
            throw RegisterValidationException(registerValidationResult)
        } else if (lastName.length < 3) {
            registerValidationResult.contactPersonLastName = false
            registerValidationResult.message = "Last Name is too short"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateAgencyFirstName(firstName: String) {
        registerValidationResult.contactPersonFirstNameValid = true
        if (firstName.isBlank()) {
            registerValidationResult.contactPersonFirstNameValid = false
            registerValidationResult.message = "First Name can not be empty"
            throw RegisterValidationException(registerValidationResult)
        } else if (firstName.length < 3) {
            registerValidationResult.contactPersonFirstNameValid = false
            registerValidationResult.message = "First Name is too short"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateAgencyName(name: String) {
        registerValidationResult.agencyName = true
        if (name.isBlank()) {
            registerValidationResult.agencyName = false
            registerValidationResult.message = "Agency Name can not be empty"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateAgencyEmail(email: String) {
        if (email.isBlank())
            registerValidationResult.contactPersonMailValid = true
        else if (!ValidationHelper.isEmailValid(email)) {
            registerValidationResult.contactPersonMailValid = false
            registerValidationResult.message = "Invalid email entered"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateEducation(education: String) {
        registerValidationResult.educationValid = true
        if (education.isBlank()) {
            registerValidationResult.educationValid = false
            registerValidationResult.message = "Entered valid education"
            throw RegisterValidationException(registerValidationResult)
        }
    }

     fun validateCity(cityId: String?) {
        registerValidationResult.cityValid = true
        if (cityId.isNullOrEmpty()) {
            registerValidationResult.cityValid = false
            registerValidationResult.message = " Select city"
            throw RegisterValidationException(registerValidationResult)
        }
    }
    fun validatePincode(pincodeId: String?) {
        registerValidationResult.pincodeValid = true
        if (pincodeId.isNullOrEmpty()) {
            registerValidationResult.pincodeValid = false
            registerValidationResult.message = "Enter Valid Pincode"
            throw RegisterValidationException(registerValidationResult)
        }
    }

    private fun validateDateOfBirth(dateOfBirth: Date?) {
        registerValidationResult.dobValid = true
        if (dateOfBirth == null) {
            registerValidationResult.dobValid = false
            registerValidationResult.message = "Select Date Of Birth"
            throw RegisterValidationException(registerValidationResult)
        } else if (dateOfBirth?.isDateOlderThanYear(1940)!!) {
            registerValidationResult.dobValid = false
            registerValidationResult.message = "Date Of Birth Is Too Old"
            throw RegisterValidationException(registerValidationResult)
        } else if (!dateOfBirth.isDateOlderThanYear(DateTimeHelper.currentYear - 18)) {
            registerValidationResult.dobValid = false
            registerValidationResult.message = "Date Of Birth Should Be At Least 18 Years Old"
            throw RegisterValidationException(registerValidationResult)
        }

    }
}