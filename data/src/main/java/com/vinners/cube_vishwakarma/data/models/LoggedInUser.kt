package com.vinners.cube_vishwakarma.data.models

import android.net.Uri

data class LoggedInUser(
    val id: String?,
    val sessionToken: String,
    val displayName: String?,
    val mobileNumber: String,
    val pic: String?,
    val email: String? = null,
    val design: String?

)