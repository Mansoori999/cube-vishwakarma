package com.vinners.trumanms.data.repository

import com.vinners.trumanms.data.dataStores.help.HelpRemoteDataStore
import com.vinners.trumanms.data.models.help.Help
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.data.models.help.QueryRequest
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