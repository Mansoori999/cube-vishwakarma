package com.vinners.cube_vishwakarma.data.models.complaints

data class StatusData(
        val name: String? = null
) {

    override fun toString(): String {
        return name!!
    }
}
//val id: String,