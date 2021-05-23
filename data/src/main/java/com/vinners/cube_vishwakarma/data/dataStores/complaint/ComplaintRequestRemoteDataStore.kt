package com.vinners.cube_vishwakarma.data.dataStores.complaint


import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.*

interface ComplaintRequestRemoteDataStore{

    suspend fun getComplaintList():List<ComplaintOutletList>

    suspend fun getComplaintTypeList():List<ComplaintTypeList>

    suspend fun getOrderByList():List<ComplaintOrderByList>

    suspend fun submitComplaintRequestData(
            typeid :String,
            work:String,
            outletid:Int,
            remarks:String,
            orderby:String
    ) : List<String>

    suspend fun getViewComplaintRequest(startDate: String,endDate: String) :List<ComplaintRequestResponse>
}