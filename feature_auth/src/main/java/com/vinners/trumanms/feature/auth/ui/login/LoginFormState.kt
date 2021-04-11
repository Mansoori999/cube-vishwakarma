package com.vinners.trumanms.feature.auth.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val mobileError: String? = null,
    val isDataValid: Boolean = false
)