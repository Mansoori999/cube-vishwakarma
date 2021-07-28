package com.vinners.cube_vishwakarma.data.dataStores.complaint

import com.vinners.cube_vishwakarma.data.models.complaints.*

interface MyComplaintRemoteDataStore {

    suspend fun getComplaintlist(userid:String): List<MyComplaintList>

    suspend fun getComplaintetails(id: String):MyComplainDetailsList

    suspend fun  upDateComplaint(statusremarks:String, status:String, id:Int,  image: List<String>): List<String>

    suspend fun allocateUserForComplaint() :List<AllocateUserResponse>

    suspend fun requestAllocatedUserForComplaint(supervisorid:String?,enduserid:String?,foremanid: String?,compid: String):List<String>
}