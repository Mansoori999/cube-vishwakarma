package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.help.HelpRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.help.Query
import com.vinners.cube_vishwakarma.data.models.help.QueryRequest
import javax.inject.Inject

class HelpRepository @Inject constructor(
    private val helpRemoteDataStore: HelpRemoteDataStore
) {

    suspend fun getQueAns(): List<Help>{
        return helpRemoteDataStore.getQueAns()
    }

    suspend fun submitQuery(queryRequest: QueryRequest,imageFile: String?){
        helpRemoteDataStore.submitQuery(queryRequest,imageFile)
    }

    suspend fun getQuery(): List<Query>{
        return helpRemoteDataStore.getQuery()
    }
}