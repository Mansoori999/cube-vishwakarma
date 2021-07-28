package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails

data class AllocateUserData (
        val id: String,
        val name: String,
        val designation:String

) {
    override fun toString(): String {
        return name
    }
}