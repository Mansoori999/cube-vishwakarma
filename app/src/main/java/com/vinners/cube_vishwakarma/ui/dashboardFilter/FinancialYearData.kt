package com.vinners.cube_vishwakarma.ui.dashboardFilter

data class FinancialYearData(
    val id: Int,
    val name: String,
    val startdate:String,
    val enddate:String
) {

    override fun toString(): String {
        return name
    }
}