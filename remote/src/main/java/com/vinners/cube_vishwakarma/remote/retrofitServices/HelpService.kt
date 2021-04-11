package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.help.Query
import com.vinners.cube_vishwakarma.data.models.help.QueryRequest
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