package com.vinners.cube_vishwakarma.data.dataStores.documents

import com.vinners.cube_vishwakarma.data.models.dashboardFilter.DashboardFilterList
import com.vinners.cube_vishwakarma.data.models.documents.DocumentsResponse

interface DocumentsRemoteDataStore {
    suspend fun getDocumentsData():List<DocumentsResponse>
}