package com.vinners.cube_vishwakarma.data.models.nearby

data class NearByResponse(
	val nearByResponse: List<NearByResponseItem> = emptyList()
)

data class NearByResponseItem(
	val contactperson: String? = null,
	val updatedby: Any? = null,
	val isactive: Boolean? = null,
	val pic: Any? = null,
	val updatedon: Any? = null,
	val secondarymail: String? = null,
	val districtid: Int? = null,
	val createdby: Int? = null,
	val salesarea: String? = null,
	val zone: String? = null,
	val id: Int? = null,
	val said: Int? = null,
	val secondarymobile: String? = null,
	val address: String? = null,
	val gps: String? = null,
	val workingcomplaint:Boolean? = null,
	val primarymail: String? = null,
	val createdon: String? = null,
	val roid: Int? = null,
	val gpsaddress: String? = null,
	val primarymobile: String? = null,
	val districtname: String? = null,
	val name: String? = null,
	val zoneid: Int? = null,
	val customercode: String? = null,
	val location: String? = null,
	val category: String? = null,
	val regionaloffice: String? = null,
	val complaintid:String?= null
)

