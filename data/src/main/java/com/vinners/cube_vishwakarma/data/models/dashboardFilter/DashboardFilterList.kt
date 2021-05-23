package com.vinners.cube_vishwakarma.data.models.dashboardFilter

import com.google.gson.annotations.SerializedName


data class DashboardFilterList(

	@field:SerializedName("enddate")
	val enddate: String? = null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("startdate")
	val startdate: String? = null
){
	override fun toString(): String {
		return name
	}
}
