package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.MyComplaintService
import javax.inject.Inject

class MyComplaintRemoteDataStoreImpl @Inject constructor(
        private val myComplaintService: MyComplaintService
): MyComplaintRemoteDataStore {

    override suspend fun getComplaintlist(userid:String): List<MyComplaintList> {
        return myComplaintService.getComplaint(
                ComplaintUserIdRequet(
                        userid = userid
                )).bodyOrThrow()
    }

    override suspend fun getComplaintetails(id: String): MyComplainDetailsList {
        return myComplaintService.getComplaintDetails(id).bodyOrThrow().first()
    }

    override suspend fun upDateComplaint(statusremarks: String, status: String, id: Int):List<String> {
        return myComplaintService.upDateComplaint(
                UpDateComplaintRequest(
                        id = id,
                        status = status,
                        statusremarks = statusremarks
                )
        ).bodyOrThrow()
    }
}