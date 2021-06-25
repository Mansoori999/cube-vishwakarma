package com.vinners.cube_vishwakarma.cache.localDataStoreImpl

import androidx.annotation.WorkerThread
import com.vinners.cube_vishwakarma.cache.OutletsLocalDatabase
import com.vinners.cube_vishwakarma.cache.dao.OutletsDao
import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletsLocalDataStore
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OutletLocalDataStoreImpl @Inject constructor(
    private val outletsDao: OutletsDao
): OutletsLocalDataStore {


    override suspend fun insertAllOutlets(outlets: List<OutletsList>) {
        return outletsDao.insertOutlet(outlets)
    }

    override suspend fun deleteAllOutlets() {
        return outletsDao.deleteAllOutlets()
    }

    override suspend fun getOutletsBYID(roid:String , said:String): List<OutletsList> {
        return outletsDao.getOutletsByID(roid,said)
    }
//    override suspend fun insertAllOutlets(): List<OutletsList> {
//        return outletsLocalDatabase.getOutletsDao().insertOutlet()
//    }


}