package com.vinners.cube_vishwakarma.data.dataStores.dashboard

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList

interface DashBoardRemoteDataStore {

    suspend fun getDashboard(): DashBoardResponse

    suspend fun getFinancialData():List<DashboardFilterList>
}