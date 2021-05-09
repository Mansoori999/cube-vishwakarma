package com.vinners.cube_vishwakarma.data.repository


import com.vinners.cube_vishwakarma.data.dataStores.complaint.ComplaintRequestRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOrderByList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintTypeList
import javax.inject.Inject

class ComplaintRequestRepository @Inject constructor(
    private val complaintRequestRemoteDataStore: ComplaintRequestRemoteDataStore
) {
    suspend fun getComplaintList() : List<ComplaintOutletList>{
       return complaintRequestRemoteDataStore.getComplaintList()
    }

    suspend fun getComplaintType() : List<ComplaintTypeList>{
        return complaintRequestRemoteDataStore.getComplaintTypeList()
    }

    suspend fun getOrderByList(): List<ComplaintOrderByList>{
        return complaintRequestRemoteDataStore.getOrderByList()
    }

    suspend fun submitComplaintRequestData(
            typeid :String,
            work:String,
            outletid:String,
            remarks:String,
            orderby:String
    ):List<String>{
        return complaintRequestRemoteDataStore.submitComplaintRequestData(typeid,work,outletid,remarks,orderby)
    }
}