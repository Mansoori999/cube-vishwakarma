package com.vinners.cube_vishwakarma.data.dataStores.nearby

import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponse
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem

interface NearByRemoteDataStore {
    suspend fun getNearBy(gpsaddress:String,range:String, type:String):List<NearByResponseItem>
}