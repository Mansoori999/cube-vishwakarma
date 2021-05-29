package com.vinners.cube_vishwakarma.data.models.documents

import com.google.gson.annotations.SerializedName

data class DocumentsResponse(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("catid")
	val catid: String? = null,

	@field:SerializedName("updatedby")
	val updatedby: Any? = null,

	@field:SerializedName("createdby")
	val createdby: Int? = null,

	@field:SerializedName("isactive")
	val isactive: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("usertypes")
	val usertypes: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("updatedon")
	val updatedon: Any? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("createdon")
	val createdon: String? = null
)
