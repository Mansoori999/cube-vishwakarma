package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.dashboard.DashBoardRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ActiveSubAdminResponse
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import javax.inject.Inject

class DashBoardRepository @Inject constructor(
    private val dashBoardRemoteDataStore: DashBoardRemoteDataStore
) {
    suspend fun getFinancialData():List<DashboardFilterList>{
        return dashBoardRemoteDataStore.getFinancialData()
    }

    suspend fun getDashBoard(startDate:String,endDate:String,regionalOfficeIds:String?,activeSubadminId:String?): DashBoardResponseDataItem {
        return dashBoardRemoteDataStore.getDashboard(startDate,endDate,regionalOfficeIds,activeSubadminId)
    }

    suspend fun activeSubAdmin():List<ActiveSubAdminResponse>{
        return dashBoardRemoteDataStore.activeSubAdmin()
    }

}