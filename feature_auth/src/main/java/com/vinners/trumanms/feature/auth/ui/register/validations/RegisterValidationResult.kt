package com.vinners.trumanms.feature.auth.ui.register.validations

class RegisterValidationResult {
//individual user basics
    var firstNameValid = true
    var lastNameValid = true
    var emailValid = true
    var dobValid = true
    var gender = true

    //individual education
    var educationValid = true
    var experienceValid = true

    //location
    var cityValid = true
    var pincodeValid = true

    //agency basics
    var contactPersonFirstNameValid = true
    var contactPersonLastName = true
    var contactPersonMailValid = true
    var contactPersonDesignationValid = true
    var agencyName = true

    //agecy work validation
    var teamSizeValid = true
    var yearInBusinessValid = true
    var languageValid = true

    var message = "This message should never be shown"

}