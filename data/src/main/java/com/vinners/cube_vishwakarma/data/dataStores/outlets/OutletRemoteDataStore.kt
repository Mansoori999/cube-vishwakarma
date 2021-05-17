package com.vinners.cube_vishwakarma.data.dataStores.outlets

import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

interface OutletRemoteDataStore {

    suspend fun getOutletData():List<OutletsList>

    suspend fun getOutletDetails(outletid : String) : OutletDetailsList

    suspend fun editOutlet(
        outletid: String?,
        secondarymail: String?,
        secondarymobile: String?,
        gps: String,
        gpsAddress: String,
        images: List<String>,
        pic:String?
    )

    suspend fun getComplaints(outletid :String):List<MyComplaintList>
}