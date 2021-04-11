package com.vinners.trumanms.remote.retrofitServices

import com.vinners.trumanms.data.models.help.Help
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.data.models.help.QueryRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface HelpService {

    @GET("api/common/categorywisefaqs")
    suspend fun getQueAns(): Response<List<Help>>

    @Multipart
    @POST("api/common/insertmobileenquiry")
    suspend fun submitQueryData(
        @Part imageFile: MultipartBody.Part?,
        @Part("data") queryRequest: QueryRequest): Response<List<String>>

    @GET("api/common/getquestionanswer")
    suspend fun getQueries(): Response<List<Query>>
}