package com.vinners.cube_vishwakarma.data.dataStores.outlets
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

interface OutletsLocalDataStore  {

//    suspend fun getOutlets(): Observable<OutletsList>
//    outlets: List<OutletsList>
    suspend fun insertAllOutlets(outlets: List<OutletsList>)

    suspend fun deleteAllOutlets()

    suspend fun getOutletsBYID(roid:List<Int> , said:List<Int>): List<OutletsList>

    suspend fun getOutletsBYIDWithOR(roid:List<Int> , said:List<Int>): List<OutletsList>

    suspend fun insertAllMyColplaints(complaints : List<MyComplaintList>)

    suspend fun deleteAllMyComplaints()

    suspend fun getAllMyComplaints():List<MyComplaintList>

    suspend fun getComplaintBYIDAND(roid:List<Int> , said:List<Int>): List<MyComplaintList>

    suspend fun getComplaintBYIDWithOR(roid:List<Int> , said:List<Int>): List<MyComplaintList>

    suspend fun getComplaintByAllIDAND(roid:List<Int> , said:List<Int>,subadmin:List<Int>): List<MyComplaintList>

    suspend fun getComplaintByIDWithSubAminOR(roid:List<Int> , said:List<Int>,subadmin:List<Int>): List<MyComplaintList>

    suspend fun getComplaintByIDWithSubadminAndRO(roid:List<Int> , subadmin:List<Int>): List<MyComplaintList>

    suspend fun insertDashboardData(dashboardList : DashBoardResponseDataItem)

    suspend fun deleteDashboardData()

    suspend fun getDashboardData():DashBoardResponseDataItem

//    fun getOutlets(): Observable<OutletsList>
}