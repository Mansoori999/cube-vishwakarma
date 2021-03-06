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



    suspend fun getOutletsBYID(roid:List<Int> , said:List<Int>):List<OutletsList> {
        return OutletsLocalDataStore.getOutletsBYID(roid,said)
    }

    suspend fun getOutletsBYIDWithOR(roid:List<Int> , said:List<Int>):List<OutletsList> {
        return OutletsLocalDataStore.getOutletsBYIDWithOR(roid,said)
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
        images: List<String>,
        pic:String?
    ){
        outletRemoteDataStore.editOutlet(
            outletid,
            secondarymail,
            secondarymobile,
            images,
            pic
        )
    }
    suspend fun editOutletGps(
        outletid: String?,
        gps: String,
        gpsAddress: String,
    ){
        outletRemoteDataStore.editOutletGps(outletid,gps,gpsAddress)
    }

    suspend fun getComplaintsByOutletid(outletid :String): List<MyComplaintList>{
        return outletRemoteDataStore.getComplaints(outletid)
    }

    suspend fun getComplaintWithStatus(status : String,startDate : String,endDate : String,regionalOfficeIds : String?,subadminIds:String?):List<MyComplaintList>{
        return outletRemoteDataStore.getComplaintWithStatus(status,startDate,endDate,regionalOfficeIds,subadminIds)
    }
}