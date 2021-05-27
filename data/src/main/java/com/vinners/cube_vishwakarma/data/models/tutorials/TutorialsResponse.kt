package com.vinners.cube_vishwakarma.data.models.tutorials

import com.google.gson.annotations.SerializedName

data class TutorialsResponse (
        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("id")
        val id: Int?=null,

        @field:SerializedName("path")
        val path: String? = null
){
}