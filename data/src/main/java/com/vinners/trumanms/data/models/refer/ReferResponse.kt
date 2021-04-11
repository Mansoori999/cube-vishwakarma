package com.vinners.trumanms.data.models.refer

import com.google.gson.annotations.SerializedName

data class ReferResponse(

	@field:SerializedName("url")
	val url: Url? = null
)

data class Url(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("fullLink")
	val fullLink: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("shortLink")
	val shortLink: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
