package com.vinners.cube_vishwakarma.remote.dataStoreImpl


import com.vinners.cube_vishwakarma.data.dataStores.complaint.ComplaintRequestRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.*
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.ComplaintRequestService
import javax.inject.Inject

class ComplaintRequestRemoteDataStoreImpl @Inject constructor(
private val complaintRequestService: ComplaintRequestService
) : ComplaintRequestRemoteDataStore {

    override suspend fun getComplaintList(): List<ComplaintOutletList> {
       return complaintRequestService.getComplaintList().bodyOrThrow()
    }

    override suspend fun getComplaintTypeList(): List<ComplaintTypeList> {
       return complaintRequestService.getComplaintTypeList().bodyOrThrow()
    }

    override suspend fun getOrderByList(): List<ComplaintOrderByList> {
        return complaintRequestService.getOrderByList().bodyOrThrow()
    }

    override suspend fun submitComplaintRequestData(
            typeid: String,
            work: String,
            outletid: Int,
            remarks: String,
            orderby: String): List<String> {
        return complaintRequestService.submitComplaintRequestData(
                ComplaintSubmitRequest(
                        typeid = typeid,
                        work = work,
                        outletid = outletid,
                        remarks = remarks,
                        orderby = orderby
                )).bodyOrThrow()
    }

    override suspend fun getViewComplaintRequest(startDate: String,endDate: String): List<ComplaintRequestResponse> {
        return complaintRequestService.getComplaintRequestViewActivityList(
            ComplaintRequestViewRequest(
                startDate = startDate,
                endDate  = endDate
            )).bodyOrThrow()
    }
}