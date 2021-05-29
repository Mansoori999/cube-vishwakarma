package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.documents.DocumentsRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse
import javax.inject.Inject

class DocumentsRepository @Inject constructor(
    private val documentsRemoteDataStore: DocumentsRemoteDataStore
) {
    suspend fun getDocumentsData():List<DocumentsResponse>{
        return documentsRemoteDataStore.getDocumentsData()
    }
}