package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.dashboard.DashBoardRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.DashBoardService
import javax.inject.Inject

class DashBoardRemoteDataStoreImpl @Inject constructor(
    private val dashBoardService: DashBoardService
) : DashBoardRemoteDataStore {

    override suspend fun getDashboard(): DashBoardResponse {
        return dashBoardService.getDashboard().bodyOrThrow().first()
    }

}