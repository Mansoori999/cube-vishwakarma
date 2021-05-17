package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.dashboard.DashBoardRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse
import javax.inject.Inject

class DashBoardRepository @Inject constructor(
    private val dashBoardRemoteDataStore: DashBoardRemoteDataStore
) {
    suspend fun getDashBoard():DashBoardResponse{
        return dashBoardRemoteDataStore.getDashboard()
    }

}