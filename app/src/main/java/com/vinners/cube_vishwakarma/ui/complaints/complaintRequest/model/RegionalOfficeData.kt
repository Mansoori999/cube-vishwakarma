package com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.model

data class RegionalOfficeData(
    val id: String,
    val name: String
) {

    override fun toString(): String {
        return name
    }
}