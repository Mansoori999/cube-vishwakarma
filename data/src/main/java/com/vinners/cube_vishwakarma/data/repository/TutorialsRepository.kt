package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.tutorials.TutorialsRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse
import javax.inject.Inject

class TutorialsRepository @Inject constructor(
        private val tutorialsRemoteDataStore: TutorialsRemoteDataStore
) {
    suspend fun getTutorialsData():List<TutorialsResponse>{
        return tutorialsRemoteDataStore.getTutorialsData()
    }
}