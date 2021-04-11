package com.vinners.trumanms.data.models

import android.net.Uri

data class LoggedInUser(
    val id: String?,
    val sessionToken: String,
    val displayName: String?,
    val mobileNumber: String,
    val empCode: String?,
    val email: String? = null
)