package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.jobs.AppliedJob
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import retrofit2.Response
import retrofit2.http.GET

interface OutletService {

    @GET("api/outlet/getoutlet")
    suspend fun getOutlet(): Response<List<OutletsList>>
}