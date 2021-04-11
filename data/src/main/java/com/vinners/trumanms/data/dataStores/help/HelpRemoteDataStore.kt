package com.vinners.trumanms.data.dataStores.help

import com.vinners.trumanms.data.models.help.Help
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.data.models.help.QueryRequest

interface HelpRemoteDataStore {

    suspend fun getQueAns(): List<Help>

    suspend fun submitQuery(queryRequest: QueryRequest,imageFile: String?)

    suspend fun getQuery(): List<Query>
}