package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.*
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DashBoardService {

    @GET("api/common/getfinancialyears")
    suspend fun getFinancialData():Response<List<DashboardFilterList>>

    @POST("api/dashboard/mydashboard")
    suspend fun getDashboard(
            @Body dashBoardRequest: DashBoardRequest
    ): Response<List<DashBoardResponseDataItem>>
//            Response<List<DashBoardResponse>>

    @GET("api/user/getuserbytype?usertype=subadmin&status=active")
    suspend fun activeSubAdmin():Response<List<ActiveSubAdminResponse>>

//    @Query("usertype") usertype:String,
//    @Query("status") status:String
}