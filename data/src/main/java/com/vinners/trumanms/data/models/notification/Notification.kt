package com.vinners.trumanms.data.models.notification

import com.google.gson.annotations.SerializedName

data class Notification(
    @field: SerializedName("id")
    val id: String? = null,

    @field: SerializedName("userid")
    val userId: String? = null,

    @field: SerializedName("textdata")
    val message: String? = null,

    @field: SerializedName("isdelivered")
    val isdelivered: Boolean,

    @field: SerializedName("deliveredon")
    val deliveredon: String? = null,

    @field: SerializedName("isread")
    val isRead: Boolean,

    @field: SerializedName("readon")
    val readon: String? = null,

    @field: SerializedName("createdon")
    val createdon: String? = null
) {
}