package com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.model

data class OutletAreaData(
    val id: Int,
    val name: String
) {

    override fun toString(): String {
        return name
    }
}