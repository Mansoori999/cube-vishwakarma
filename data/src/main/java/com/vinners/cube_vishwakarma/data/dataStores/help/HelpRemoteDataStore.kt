package com.vinners.cube_vishwakarma.data.dataStores.help

import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.help.Query
import com.vinners.cube_vishwakarma.data.models.help.QueryRequest

interface HelpRemoteDataStore {

    suspend fun getQueAns(): List<Help>

    suspend fun submitQuery(queryRequest: QueryRequest,imageFile: String?)

    suspend fun getQuery(): List<Query>
}