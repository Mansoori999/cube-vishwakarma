package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.documents.DocumentsRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.DocumentsService
import javax.inject.Inject

class DocumentsRemoteDataStoreImpl @Inject constructor(
    private val documentsService : DocumentsService
):DocumentsRemoteDataStore {

    override suspend fun getDocumentsData(): List<DocumentsResponse> {
       return documentsService.getDocumentsData().bodyOrThrow()
    }
}