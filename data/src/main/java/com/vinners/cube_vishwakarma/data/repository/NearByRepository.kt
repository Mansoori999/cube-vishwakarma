package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.nearby.NearByRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponse
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem
import javax.inject.Inject

class NearByRepository @Inject constructor(
    private val nearByRemoteDataStore: NearByRemoteDataStore
) {
    suspend fun getNearByMap(gpsaddress:String,range:String, type:String):List<NearByResponseItem>{
        return nearByRemoteDataStore.getNearBy(gpsaddress,range,type)
    }
}