package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.MyComplaintService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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

    override suspend fun upDateComplaint(statusremarks: String, status: String, id: Int,  image: List<String>):List<String> {
        val imagesBodies = if (image.isEmpty())
            emptyList()
        else {
            image.map {
                val file = File(it)
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            }
        }
        return myComplaintService.upDateComplaint(
                UpDateComplaintRequest(
                        id = id,
                        status = status,
                        statusremarks = statusremarks
                ),
                        image = imagesBodies
        ).bodyOrThrow()
    }

    override suspend fun allocateUserForComplaint(): List<AllocateUserResponse> {
        return myComplaintService.allocateUserForComplaint().bodyOrThrow()
    }

    override suspend fun requestAllocatedUserForComplaint(supervisorid: String?, enduserid: String?, foremanid: String?, compid: String): List<String> {
        return myComplaintService.requestAllocatedUserForComplaint(
                AllocateUserRequestForComplaint(
                        supervisorid = supervisorid,
                        enduserid = enduserid,
                        foremanid = foremanid,
                        compid = compid
                )
        ).bodyOrThrow()
    }

}