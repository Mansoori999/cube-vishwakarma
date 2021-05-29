package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import retrofit2.Response
import retrofit2.http.GET

interface DocumentsService {

    @GET("api/master/getdocument")
    suspend fun getDocumentsData():Response<List<DocumentsResponse>>
}