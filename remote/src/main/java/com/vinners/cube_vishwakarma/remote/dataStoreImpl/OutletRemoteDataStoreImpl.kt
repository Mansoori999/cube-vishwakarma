package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.ComplaintUserIdRequet
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.dashboard.ComplaintRequestWithStatus
import com.vinners.cube_vishwakarma.data.models.outlets.ComplaintRequest
import com.vinners.cube_vishwakarma.data.models.outlets.EditOutletRequest
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.OutletService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class OutletRemoteDataStoreImpl @Inject constructor(
        private val outletService: OutletService
) : OutletRemoteDataStore{
    override suspend fun getOutletData(): List<OutletsList> {
        return outletService.getOutlet().bodyOrThrow()
    }

    override suspend fun getOutletDetails(outletid: String): OutletDetailsList {
        return  outletService.getOutletDetails(outletid).bodyOrThrow().first()
    }

    override suspend fun editOutlet(
        outletid: String?,
        secondarymail: String?,
        secondarymobile: String?,
        gps: String,
        gpsAddress: String,
        images: List<String>,
        pic:String?
    ){
        val imagesBodies = if (images.isEmpty())
            emptyList()
        else {
            images.map {
                val file = File(it)
                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("file", file.name, requestFile)
            }
        }

        val response = outletService.editOutlet(
            EditOutletRequest(
             outletid = outletid,
             secondarymail = secondarymail,
             secondarymobile = secondarymobile,
             gps = gps,
             gpsaddress = gpsAddress,
             pic = pic
            ),
            images = imagesBodies
        )
        val outletId = response.bodyOrThrow().first().outletId

    }

    override suspend fun getComplaints(outletid: String): List<MyComplaintList> {
        return outletService.getComplaintsByOutletid(
            ComplaintRequest(
              outletid = outletid
            )
        ).bodyOrThrow()
    }

    override suspend fun getComplaintWithStatus(status : String,startDate : String,endDate : String,regionalOfficeIds : String?): List<MyComplaintList> {
        return outletService.getComplaintWithStatus(
            ComplaintRequestWithStatus(
                status = status,
                startDate = startDate,
                endDate = endDate,
                regionalOfficeIds = regionalOfficeIds
            )
        ).bodyOrThrow()
    }

}