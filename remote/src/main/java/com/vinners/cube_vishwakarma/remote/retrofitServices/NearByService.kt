package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.nearby.NearByRequest
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponse
import com.vinners.cube_vishwakarma.data.models.nearby.NearByResponseItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NearByService {

    @POST("api/outlet/nearbyoutletsbygps")
    suspend fun getNearBy(
        @Body nearByRequest: NearByRequest
    ): Response<List<NearByResponseItem>>
}