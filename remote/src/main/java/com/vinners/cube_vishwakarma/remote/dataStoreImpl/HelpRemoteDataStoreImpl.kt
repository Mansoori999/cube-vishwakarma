package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.help.HelpRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.help.Query
import com.vinners.cube_vishwakarma.data.models.help.QueryRequest
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.HelpService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class HelpRemoteDataStoreImpl @Inject constructor(
    private val helpService: HelpService
): HelpRemoteDataStore {

    override suspend fun getQueAns(): List<Help> {
        return helpService.getQueAns().bodyOrThrow()
    }

    override suspend fun submitQuery(queryRequest: QueryRequest,imageUrl: String?) {
        var imagePart: MultipartBody.Part? = null
        if (imageUrl.isNullOrEmpty().not()){
            val imageFile = File(imageUrl)
            val imageRequestFile = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            imagePart = MultipartBody.Part.createFormData(
                "file",
                imageFile.name,
                imageRequestFile
            )
        }
        helpService.submitQueryData(imagePart,queryRequest).bodyOrThrow()
    }

    override suspend fun getQuery(): List<Query> {
        return helpService.getQueries().bodyOrThrow()
    }
}