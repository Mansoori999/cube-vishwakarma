package com.vinners.cube_vishwakarma.data.dataStores.complaint

import com.vinners.cube_vishwakarma.data.models.complaints.ComplaintUserIdRequet
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplainDetailsList
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.complaints.UpDateComplaintList

interface MyComplaintRemoteDataStore {

    suspend fun getComplaintlist(userid:String): List<MyComplaintList>

    suspend fun getComplaintetails(id: String):MyComplainDetailsList

    suspend fun  upDateComplaint(statusremarks:String, status:String, id:Int): List<String>
}