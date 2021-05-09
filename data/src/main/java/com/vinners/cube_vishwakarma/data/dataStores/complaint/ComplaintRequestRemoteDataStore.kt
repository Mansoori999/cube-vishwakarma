package com.vinners.cube_vishwakarma.data.dataStores.complaint


import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOrderByList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintOutletList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintTypeList

interface ComplaintRequestRemoteDataStore{

    suspend fun getComplaintList():List<ComplaintOutletList>

    suspend fun getComplaintTypeList():List<ComplaintTypeList>

    suspend fun getOrderByList():List<ComplaintOrderByList>

    suspend fun submitComplaintRequestData(
            typeid :String,
            work:String,
            outletid:String,
            remarks:String,
            orderby:String
    ) : List<String>
}