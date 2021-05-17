package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import java.io.File
import javax.inject.Inject

class OutletRepository @Inject constructor(
        private val outletRemoteDataStore: OutletRemoteDataStore
) {

    suspend fun getOutletData():List<OutletsList>{
        return outletRemoteDataStore.getOutletData()
    }

    suspend fun getOutletDetails(outletid : String) : OutletDetailsList{
        return outletRemoteDataStore.getOutletDetails(outletid)
    }

    suspend fun editOutlet(
        outletid: String?,
        secondarymail: String?,
        secondarymobile: String?,
        gps: String,
        gpsAddress: String,
        images: List<String>,
        pic:String?
    ){
        outletRemoteDataStore.editOutlet(
            outletid,
            secondarymail,
            secondarymobile,
            gps,
            gpsAddress,
            images,
            pic
        )
    }

    suspend fun getComplaintsByOutletid(outletid :String): List<MyComplaintList>{
        return outletRemoteDataStore.getComplaints(outletid)
    }

    suspend fun getComplaintWithStatus(status : String):List<MyComplaintList>{
        return outletRemoteDataStore.getComplaintWithStatus(status)
    }
}