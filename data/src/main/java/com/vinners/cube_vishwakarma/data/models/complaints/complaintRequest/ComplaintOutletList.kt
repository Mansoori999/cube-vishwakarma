package com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest

import com.google.gson.annotations.SerializedName

data class ComplaintOutletList(

	@field:SerializedName("contactperson")
	val contactperson: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("outletid")
	val outletid: Int? = null,

	@field:SerializedName("gps")
	val gps: String? = null,

	@field:SerializedName("primarymail")
	val primarymail: String? = null,

	@field:SerializedName("roid")
	val roid: Int? = null,

	@field:SerializedName("secondarymail")
	val secondarymail: String? = null,

	@field:SerializedName("districtid")
	val districtid: Int? = null,

	@field:SerializedName("primarymobile")
	val primarymobile: String? = null,

	@field:SerializedName("salesarea")
	val salesarea: String? = null,

	@field:SerializedName("zone")
	val zone: String? = null,

	@field:SerializedName("district")
	val district: String? = null,

	@field:SerializedName("zoneid")
	val zoneid: Int? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("customercode")
	val customercode: String? = null,

	@field:SerializedName("outlet")
	val outlet: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("said")
	val said: Int? = null,

	@field:SerializedName("regionaloffice")
	val regionaloffice: String? = null,

	@field:SerializedName("secondarymobile")
	val secondarymobile: String? = null
)
