package com.vinners.cube_vishwakarma.cache.localDataStoreImpl

import androidx.annotation.WorkerThread
import com.vinners.cube_vishwakarma.cache.OutletsLocalDatabase
import com.vinners.cube_vishwakarma.cache.dao.DashboardDao
import com.vinners.cube_vishwakarma.cache.dao.MyComplaintDao
import com.vinners.cube_vishwakarma.cache.dao.OutletsDao
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletsLocalDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponseDataItem
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OutletLocalDataStoreImpl @Inject constructor(
    private val outletsDao: OutletsDao,
    private val myComplaintDao: MyComplaintDao,
    private val dashboardDao: DashboardDao
): OutletsLocalDataStore {


    override suspend fun insertAllOutlets(outlets: List<OutletsList>) {
        return outletsDao.insertOutlet(outlets)
    }

    override suspend fun deleteAllOutlets() {
        return outletsDao.deleteAllOutlets()
    }

    override suspend fun getOutletsBYID(roid: List<Int>, said: List<Int>): List<OutletsList> {
        return outletsDao.getOutletsByID(roid,said)
    }

    override suspend fun getOutletsBYIDWithOR(roid: List<Int>, said: List<Int>): List<OutletsList> {
        return outletsDao.getOutletsByIDWithOR(roid,said)
    }

    override suspend fun insertAllMyColplaints(complaints: List<MyComplaintList>) {
        return myComplaintDao.insertMyComplaints(complaints)
    }

    override suspend fun deleteAllMyComplaints() {
        return myComplaintDao.deleteAllMyComplaints()
    }

    override suspend fun getAllMyComplaints(): List<MyComplaintList> {
        return myComplaintDao.getAllComplaints()
    }

    override suspend fun getComplaintBYIDAND(roid: List<Int>, said: List<Int>): List<MyComplaintList> {
        return myComplaintDao.getComplaintByIDAND(roid,said)
    }

    override suspend fun getComplaintBYIDWithOR(roid: List<Int>, said: List<Int>): List<MyComplaintList> {
        return myComplaintDao.getComplaintByIDWithOR(roid,said)
    }

    override suspend fun getComplaintByAllIDAND(roid: List<Int>, said: List<Int>, subadmin: List<Int>): List<MyComplaintList> {
        return myComplaintDao.getComplaintByAllID(roid,said,subadmin)
    }

    override suspend fun getComplaintByIDWithSubAminOR(roid: List<Int>, said: List<Int>, subadmin: List<Int>): List<MyComplaintList> {
        return myComplaintDao.getComplaintByIDWithSubAmin(roid,said,subadmin)
    }

    override suspend fun getComplaintByIDWithSubadminAndRO(roid: List<Int>, subadmin: List<Int>): List<MyComplaintList> {
        return myComplaintDao.getComplaintByIDWithSubadminAndRO(roid,subadmin)
    }

    override suspend fun insertDashboardData(dashboardList: DashBoardResponseDataItem) {
        return dashboardDao.insertDashboardData(dashboardList)
    }

    override suspend fun deleteDashboardData() {
        return dashboardDao.deleteDashboardData()
    }

    override suspend fun getDashboardData(): DashBoardResponseDataItem {
        return dashboardDao.getDashboardAllData()
    }


}