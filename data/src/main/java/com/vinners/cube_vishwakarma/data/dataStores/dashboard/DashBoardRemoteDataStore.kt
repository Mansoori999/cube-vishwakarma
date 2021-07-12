package com.vinners.cube_vishwakarma.data.dataStores.dashboard

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ActiveSubAdminResponse
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList

interface DashBoardRemoteDataStore {

    suspend fun getFinancialData():List<DashboardFilterList>

    suspend fun getDashboard(startDate:String,endDate:String,regionalOfficeIds:String?,activeSubadminId:String?): DashBoardResponseDataItem

    suspend fun activeSubAdmin():List<ActiveSubAdminResponse>

}