package com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest

import com.google.gson.annotations.SerializedName

data class ComplaintTypeList(

	@field:SerializedName("createdby")
	val createdby: String? = null,

	@field:SerializedName("isactive")
	val isactive: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("createdon")
	val createdon: String? = null,

	@field:SerializedName("isearthing")
	val isearthing: Boolean? = null
)

