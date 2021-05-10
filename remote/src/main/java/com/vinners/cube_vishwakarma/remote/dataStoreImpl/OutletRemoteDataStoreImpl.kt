package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.outlets.OutletRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.outlets.OutletDetailsList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.OutletService
import javax.inject.Inject

class OutletRemoteDataStoreImpl @Inject constructor(
        private val outletService: OutletService
) : OutletRemoteDataStore{
    override suspend fun getOutletData(): List<OutletsList> {
        return outletService.getOutlet().bodyOrThrow()
    }

    override suspend fun getOutletDetails(outletid: String): OutletDetailsList {
        return  outletService.getOutletDetails(outletid).bodyOrThrow().first()
    }

}