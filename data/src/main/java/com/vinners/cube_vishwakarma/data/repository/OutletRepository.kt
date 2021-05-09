package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import javax.inject.Inject

class OutletRepository @Inject constructor(
        private val outletRemoteDataStore: OutletRemoteDataStore
) {

    suspend fun getOutletData():List<OutletsList>{
        return outletRemoteDataStore.getOutletData()
    }
}