package com.vinners.cube_vishwakarma.ui.dashboardFilter

data class ActiveSubAdminData (
        val id: String,
        val name: String,

) {
    override fun toString(): String {
        return name
    }
}