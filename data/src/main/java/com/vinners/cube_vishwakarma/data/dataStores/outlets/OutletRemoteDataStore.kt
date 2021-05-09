package com.vinners.cube_vishwakarma.data.dataStores.outlets

import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList

interface OutletRemoteDataStore {

    suspend fun getOutletData():List<OutletsList>
}