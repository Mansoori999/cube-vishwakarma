package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.jobs.AppliedJob
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OutletService {

    @GET("api/outlet/getoutlet")
    suspend fun getOutlet(): Response<List<OutletsList>>

    @GET("api/outlet/getoutletdetailsbyid")
    suspend fun getOutletDetails(
        @Query("outletid") outletid:String):Response<List<OutletDetailsList>>
}