package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.*
import javax.inject.Inject

class MyComplaintRepository @Inject constructor(
        private val myComplaintRemoteDataStore: MyComplaintRemoteDataStore
) {
    suspend fun getComplaint(userid:String): List<MyComplaintList> {
        return myComplaintRemoteDataStore.getComplaintlist(userid)
    }

    suspend fun getComplaintDetails(id :String):MyComplainDetailsList{
        return myComplaintRemoteDataStore.getComplaintetails(id)
    }
    suspend fun upDateComplaints(statusremarks:String, status:String, id:Int):List<String>{
        return myComplaintRemoteDataStore.upDateComplaint(statusremarks,status,id)
    }
}