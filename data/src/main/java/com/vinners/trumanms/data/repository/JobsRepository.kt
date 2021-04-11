package com.vinners.trumanms.data.repository

import com.vinners.trumanms.data.dataStores.jobs.JobsRemoteDataStore
import com.vinners.trumanms.data.models.jobs.*
import com.vinners.trumanms.data.models.notes.Notes
import com.vinners.trumanms.data.models.notes.NotesRequest
import javax.inject.Inject

class JobsRepository @Inject constructor(
    private val jobsRemoteDataStore: JobsRemoteDataStore
) {

    suspend fun getJobList(pageNo: Int, cityId: String?): List<Job> {
        return jobsRemoteDataStore.getJobList(pageNo, cityId)
    }

    suspend fun getAppliedJobs(): List<AppliedJob> {
        return jobsRemoteDataStore.getAppliedJobs()
    }

    suspend fun saveJob(jobId: String) {
        jobsRemoteDataStore.saveJob(jobId)
    }

    suspend fun acceptJob(applicationId: String) {
        jobsRemoteDataStore.acceptJob(applicationId)
    }

    suspend fun getSavedJobs(): List<SavedJob> {
        return jobsRemoteDataStore.getSavedJobs()
    }

    suspend fun applyJob(jobId: String) {
        jobsRemoteDataStore.applyJob(jobId)
    }

    suspend fun withdrawApplication(
        applicationId: String,
        withdrawReason: String?
    ) {
        jobsRemoteDataStore.withdrawApplication(applicationId, withdrawReason)
    }

    suspend fun getInHandJobs(): List<Application> {
        return jobsRemoteDataStore.getInHandJobs()
    }

    suspend fun getMyOffers(): List<Application> {
        return jobsRemoteDataStore.getMyOffers()
    }

    suspend fun getJobDetails(jobId: String?,shareToken: String?): JobDetails{
        return jobsRemoteDataStore.getJobDetails(jobId,shareToken)
    }

    suspend fun unSaveJob(jobId: String){
        jobsRemoteDataStore.unsaveJob(jobId)
    }

    suspend fun abandonTask(applicationId: String,type: String,reason: String){
        jobsRemoteDataStore.abandonTask(applicationId, type, reason)
    }

    suspend fun updateStatusTask(applicationId: String,type: String,status: String){
        jobsRemoteDataStore.updateStatusTask(applicationId, type, status)
    }

    suspend fun passTask(applicationId: String,type: String){
        jobsRemoteDataStore.passTask(applicationId, type)
    }

    suspend fun getNotes(applicationId: String): List<Notes>{
      return  jobsRemoteDataStore.getNotes(applicationId)
    }

    suspend fun submitNotes(notesRequest: NotesRequest){
        jobsRemoteDataStore.submitNotes(notesRequest)
    }

    suspend fun createShareLink(jobDetailsShareLink: JobDetailsShareLink): String{
        return jobsRemoteDataStore.createShareLink(jobDetailsShareLink)
    }
}