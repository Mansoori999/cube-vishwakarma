package com.vinners.trumanms.feature.auth.ui.register.validations

import com.vinners.trumanms.core.ValidationException

class RegisterValidationException(
     registerValidationResult: RegisterValidationResult
): ValidationException(registerValidationResult.message) {
    val result = registerValidationResult
}