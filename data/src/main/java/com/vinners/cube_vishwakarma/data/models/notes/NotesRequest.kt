package com.vinners.cube_vishwakarma.data.models.notes

import com.google.gson.annotations.SerializedName

data class NotesRequest(

    @SerializedName("applicationid")
    val applicationId: String?,

    @SerializedName("notes")
    val notes: String
) {
}