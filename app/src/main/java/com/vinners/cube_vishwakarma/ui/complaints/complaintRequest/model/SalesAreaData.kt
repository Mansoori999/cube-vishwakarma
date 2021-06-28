package com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.model

data class SalesAreaData(
    val id: Int?,
    val name: String
) {

    override fun toString(): String {
        return name
    }
}