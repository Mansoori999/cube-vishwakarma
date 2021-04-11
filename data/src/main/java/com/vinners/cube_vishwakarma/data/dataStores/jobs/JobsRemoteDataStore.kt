package com.vinners.cube_vishwakarma.data.dataStores.jobs

import com.vinners.cube_vishwakarma.data.models.jobs.*
import com.vinners.cube_vishwakarma.data.models.notes.Notes
import com.vinners.cube_vishwakarma.data.models.notes.NotesRequest

interface JobsRemoteDataStore {

    suspend fun getJobList(pageNo: Int,cityId: String?): List<Job>

    suspend fun getAppliedJobs(): List<AppliedJob>

    suspend fun applyJob(jobId: String)

    suspend fun getSavedJobs(): List<SavedJob>

    suspend fun saveJob(jobId: String)

    suspend fun acceptJob(applicationId: String)

    suspend fun withdrawApplication(applicationId: String,
                                    withdrawReason: String?)

    suspend fun getInHandJobs(): List<Application>

    suspend fun getMyOffers(): List<Application>

    suspend fun getJobDetails(jobId: String?,shareToken: String?): JobDetails

    suspend fun unsaveJob(jobId: String)

    suspend fun abandonTask(applicationId: String,type: String,reason: String)

    suspend fun updateStatusTask(applicationId: String,type: String,status: String)

    suspend fun passTask(applicationId: String,type: String)

    suspend fun getNotes(applicationId: String): List<Notes>

    suspend fun submitNotes(notesRequest: NotesRequest)

    suspend fun createShareLink(jobDetailsShareLink: JobDetailsShareLink): String
}