package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletsLocalDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import javax.inject.Inject

class MyComplaintRepository @Inject constructor(
        private val myComplaintRemoteDataStore: MyComplaintRemoteDataStore,
        private val OutletsLocalDataStore: OutletsLocalDataStore
) {

    suspend fun getComplaint(userid:String): List<MyComplaintList> {
        val myComplaints= myComplaintRemoteDataStore.getComplaintlist(userid)
        OutletsLocalDataStore.deleteAllMyComplaints()
        OutletsLocalDataStore.insertAllMyColplaints(myComplaints)
        return myComplaints
    }

    suspend fun getAllMyComplaintsDao():List<MyComplaintList>{
        return OutletsLocalDataStore.getAllMyComplaints()
    }

    suspend fun getComplaintDetails(id :String):MyComplainDetailsList{
        return myComplaintRemoteDataStore.getComplaintetails(id)
    }
    suspend fun upDateComplaints(statusremarks: String,
                                 status: String,
                                 id: Int,
                                 letterPic:String,
                                 measurementPic:String,
                                 layoutPic:String
    ):List<String>{
        return myComplaintRemoteDataStore.upDateComplaint(statusremarks,status,id,letterPic,measurementPic,layoutPic)
    }
    suspend fun allocateUserForComplaint():List<AllocateUserResponse>{
        return myComplaintRemoteDataStore.allocateUserForComplaint()
    }

    suspend fun requestAllocatedUserForComplaint(supervisorid:String?,enduserid:String?,foremanid: String?,compid: String):List<String>{
        return myComplaintRemoteDataStore.requestAllocatedUserForComplaint(supervisorid,enduserid,foremanid,compid)
    }
}