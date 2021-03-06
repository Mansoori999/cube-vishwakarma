package com.vinners.cube_vishwakarma.data.models.stateAndCity

import com.google.gson.annotations.SerializedName

data class CityAndPincode(

    @SerializedName("districtList")
    val cityList: List<City>,

    @SerializedName("pincodeList")
    val pincodeList: List<Pincode>
) {
}