package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.nearby.NearByRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.nearby.NearByRequest
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponse
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.NearByService
import javax.inject.Inject

class NearByRemoteDataStoreImpl @Inject constructor(
    private val nearByService: NearByService
):NearByRemoteDataStore {

    override suspend fun getNearBy(gpsaddress:String,range:String, type:String): List<NearByResponseItem> {
       return nearByService.getNearBy(
            NearByRequest(
                gpsaddress = gpsaddress,
                range = range,
                type = type
            )).bodyOrThrow()
    }

}