package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ComplaintRequestWithStatus
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardRequest
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashBoardService {

    @GET("api/common/getfinancialyears")
    suspend fun getFinancialData():Response<List<DashboardFilterList>>

    @POST("api/dashboard/mydashboard")
    suspend fun getDashboard(
            @Body dashBoardRequest: DashBoardRequest
    ): Response<List<DashBoardResponse>>



}