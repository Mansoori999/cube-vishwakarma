package com.vinners.cube_vishwakarma.data.dataStores.outlets
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

interface OutletsLocalDataStore  {

//    suspend fun getOutlets(): Observable<OutletsList>
//    outlets: List<OutletsList>
    suspend fun insertAllOutlets(outlets: List<OutletsList>)

    suspend fun deleteAllOutlets()

    suspend fun getOutletsBYID(roid:String , said:String): List<OutletsList>
//    fun getOutlets(): Observable<OutletsList>
}