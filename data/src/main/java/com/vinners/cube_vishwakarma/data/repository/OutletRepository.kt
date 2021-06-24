package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletsLocalDataStore
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class OutletRepository @Inject constructor(
        private val outletRemoteDataStore: OutletRemoteDataStore,
        private val OutletsLocalDataStore: OutletsLocalDataStore

) {



    suspend fun getOutletsBYID(regionalOffice: String, salesArea: String):List<OutletsList> {
        return OutletsLocalDataStore.getOutletsBYID(regionalOffice,salesArea)
    }

//    suspend fun insertOutlets(outlets : List<OutletsList>){
//        return OutletsLocalDataStore.insertAllOutlets(outlets)
//    }

    suspend fun getOutletData():List<OutletsList>{
//        return outletRemoteDataStore.getOutletData()
       val outlets = outletRemoteDataStore.getOutletData()
        OutletsLocalDataStore.deleteAllOutlets()
        OutletsLocalDataStore.insertAllOutlets(outlets)
        return outlets
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

    suspend fun getComplaintWithStatus(status : String,startDate : String,endDate : String,regionalOfficeIds : String?):List<MyComplaintList>{
        return outletRemoteDataStore.getComplaintWithStatus(status,startDate,endDate,regionalOfficeIds)
    }
}