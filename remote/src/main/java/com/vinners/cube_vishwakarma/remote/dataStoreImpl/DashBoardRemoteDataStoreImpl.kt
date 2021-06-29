package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.dashboard.DashBoardRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ActiveSubAdminResponse
import com.vinners.cube_vishwakarma.data.models.dashboard.ComplaintRequestWithStatus
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardRequest
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.DashBoardService
import retrofit2.Response
import javax.inject.Inject

class DashBoardRemoteDataStoreImpl @Inject constructor(
    private val dashBoardService: DashBoardService
) : DashBoardRemoteDataStore {

    override suspend fun getFinancialData(): List<DashboardFilterList> {
        return dashBoardService.getFinancialData().bodyOrThrow()
    }

    override suspend fun getDashboard(
            startDate:String,
            endDate:String,
            regionalOfficeIds:String?,
            activeSubadminId:String?
    ): DashBoardResponse {
        return dashBoardService.getDashboard(
                DashBoardRequest(
                        startDate = startDate,
                        endDate = endDate,
                        regionalOfficeIds = regionalOfficeIds,
                        subadminIds = activeSubadminId
                )
        ).bodyOrThrow().first()
    }

    override suspend fun activeSubAdmin(): List<ActiveSubAdminResponse> {
        return dashBoardService.activeSubAdmin().bodyOrThrow()
    }


}