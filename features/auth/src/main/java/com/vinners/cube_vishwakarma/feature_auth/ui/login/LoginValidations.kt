package com.vinners.cube_vishwakarma.feature_auth.ui.login

import android.content.Context
import com.vinners.cube_vishwakarma.core.ValidationException
import com.vinners.cube_vishwakarma.feature_auth.R
import javax.inject.Inject

class LoginValidations @Inject constructor(private val context: Context) {

    fun isUserNameValid(userName: String): Boolean {

        if (userName.isNotEmpty())
            return true
        else
            throw ValidationException(context.getString(R.string.text_invalid_mobile_number))
    }

    fun isPasswordValid(password: String): Boolean {

        if (password.length > 3)
            return true
        else
            throw ValidationException(context.getString(R.string.invalid_password))

    }

}