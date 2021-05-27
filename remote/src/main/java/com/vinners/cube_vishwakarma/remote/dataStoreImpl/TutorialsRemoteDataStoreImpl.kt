package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.tutorials.TutorialsRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.TutorialsService
import javax.inject.Inject

class TutorialsRemoteDataStoreImpl @Inject constructor(
        private val tutorialsService: TutorialsService
) : TutorialsRemoteDataStore{
    override suspend fun getTutorialsData(): List<TutorialsResponse> {
       return tutorialsService.getTutorialsData().bodyOrThrow()
    }

}