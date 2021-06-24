package com.vinners.cube_vishwakarma.ui.dashboardFilter

data class SalesAreaFilterData(
    val id: Int,
    val name: String,
    var isSelected: Boolean
) {

    override fun toString(): String {
        return name
    }

}