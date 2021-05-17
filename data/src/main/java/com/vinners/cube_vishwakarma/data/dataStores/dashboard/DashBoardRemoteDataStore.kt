package com.vinners.cube_vishwakarma.data.dataStores.dashboard

import com.vinners.cube_vishwakarma.data.models.dashboard.DashBoardResponse

interface DashBoardRemoteDataStore {

    suspend fun getDashboard(): DashBoardResponse
}