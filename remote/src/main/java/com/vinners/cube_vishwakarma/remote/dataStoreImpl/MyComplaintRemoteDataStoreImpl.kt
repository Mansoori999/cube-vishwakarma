package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.complaint.MyComplaintRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.*
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.MyComplaintService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
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

    override suspend fun upDateComplaint(
            statusremarks: String,
            status: String,
            id: Int,
            letterPic:String?,
            measurementPic:String?,
            layoutPic:String?

    ):List<String> {
//        val letterpic = File(letterPic)
//        val measurementpic = File(measurementPic)
//        val layoutpic = File(layoutPic)

//        if (!letterpic.exists())
//            throw FileNotFoundException("Invalid File Path Provided For CheckIn Picture,file ${letterpic.absolutePath} does not exist or is inaccessible to app")
//        var bodyletterpic: MultipartBody.Part? = null
//        var bodymeasurementpic:MultipartBody.Part? = null
//        var bodylayoutpic:MultipartBody.Part? = null


        val bodyletterpic = if (letterPic.isNullOrEmpty())
           null
        else {
            val letterpic = File(letterPic)
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), letterpic)
            MultipartBody.Part.createFormData("file", letterpic.name, requestFile)

        }
        val bodymeasurementpic = if (measurementPic.isNullOrEmpty())
            null
        else {
            val measurementpic = File(measurementPic)
            val requestmeasurementpic = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), measurementpic)
            MultipartBody.Part.createFormData("measurementPic", measurementpic.name, requestmeasurementpic)

        }
        val bodylayoutpic = if (layoutPic.isNullOrEmpty())
            null
        else {
            val layoutpic = File(layoutPic)
            val requestlayoutpic = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), layoutpic)
            MultipartBody.Part.createFormData("layoutPic", layoutpic.name, requestlayoutpic)

        }
//        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), letterpic)
//        val bodyletterpic = MultipartBody.Part.createFormData("file", letterpic.name, requestFile)


//        val requestmeasurementpic = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), measurementpic)
//        val bodymeasurementpic = MultipartBody.Part.createFormData("measurementPic", measurementpic.name, requestmeasurementpic)

//        val requestlayoutpic = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), layoutpic)
//        val bodylayoutpic = MultipartBody.Part.createFormData("layoutPic", layoutpic.name, requestlayoutpic)

//        val imagesBodies = if (letterPic.isEmpty())
//            emptyList()
//        else {
//            image.map {
//                val file = File(it)
//                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                MultipartBody.Part.createFormData("file", file.name, requestFile)
//            }
//        }
        return myComplaintService.upDateComplaint(
                UpDateComplaintRequest(
                        id = id,
                        status = status,
                        statusremarks = statusremarks
                ),
                bodyletterpic,bodymeasurementpic,bodylayoutpic
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