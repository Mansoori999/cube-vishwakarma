package com.vinners.cube_vishwakarma.ui.dashboardFilter

data class RegionalOfficeFilterData(
    val id: Int,
    val name: String,
    var isSelected: Boolean
) {

    override fun toString(): String {
        return name
    }

}