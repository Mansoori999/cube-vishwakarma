package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.jobs.JobsRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.jobs.*
import com.vinners.cube_vishwakarma.data.models.notes.Notes
import com.vinners.cube_vishwakarma.data.models.notes.NotesRequest
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.JobsService
import javax.inject.Inject

class JobsRemotedataStoteImp @Inject constructor(
    private val jobService: JobsService
) : JobsRemoteDataStore {

    override suspend fun getJobList(pageNo: Int, cityId: String?): List<Job> {
        return jobService.getJobList(pageNo, cityId).bodyOrThrow()
    }

    override suspend fun getAppliedJobs(): List<AppliedJob> {
        return jobService.getApplication().bodyOrThrow()
    }

    override suspend fun applyJob(jobId: String) {
        jobService.applyJob(jobId).bodyOrThrow()
    }

    override suspend fun getSavedJobs(): List<SavedJob> {
        return jobService.getSavedJobs().bodyOrThrow()
    }

    override suspend fun saveJob(jobId: String) {
        jobService.saveJob(jobId).bodyOrThrow()
    }

    override suspend fun acceptJob(applicationId: String) {
        jobService.acceptJob(applicationId)
    }

    override suspend fun withdrawApplication(applicationId: String, withdrawReason: String?) {
        jobService.withdrawApplication(
            ApplicationWithdraw(
                applicationId = applicationId,
                withdrawReason = withdrawReason
            )
        )
    }

    override suspend fun getInHandJobs(): List<Application> {
        return jobService.getInHandsJobs().bodyOrThrow()
    }

    override suspend fun getMyOffers(): List<Application> {
        return jobService.getMyOffers().bodyOrThrow()
    }

    override suspend fun getJobDetails(jobId: String?,shareToken: String?): JobDetails {
        return jobService.getJobDetails(jobId,shareToken).bodyOrThrow().first()
    }

    override suspend fun unsaveJob(jobId: String) {
         jobService.unsaveJob(jobId)
    }

    override suspend fun abandonTask(applicationId: String, type: String, reason: String) {
        jobService.abandonTask(applicationId,type,reason).bodyOrThrow()
    }

    override suspend fun getNotes(applicationId: String): List<Notes> {
        return jobService.getNotes(applicationId).bodyOrThrow()
    }

    override suspend fun submitNotes(notesRequest: NotesRequest) {
        jobService.submitNotes(notesRequest)
    }

    override suspend fun updateStatusTask(applicationId: String, type: String, status: String) {
        jobService.updateStatusTask(applicationId,type, status)
    }

    override suspend fun passTask(applicationId: String, type: String) {
        jobService.passTask(applicationId,type)
    }

    override suspend fun createShareLink(jobDetailsShareLink: JobDetailsShareLink): String {
        return jobService.createShareLink(jobDetailsShareLink).bodyOrThrow().first()
    }
}