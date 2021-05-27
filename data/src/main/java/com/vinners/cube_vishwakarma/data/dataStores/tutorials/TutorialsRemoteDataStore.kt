package com.vinners.cube_vishwakarma.data.dataStores.tutorials

import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse

interface TutorialsRemoteDataStore {

    suspend fun getTutorialsData():List<TutorialsResponse>
}