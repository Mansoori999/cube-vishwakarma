package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.jobs.*
import com.vinners.cube_vishwakarma.data.models.notes.Notes
import com.vinners.cube_vishwakarma.data.models.notes.NotesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JobsService {

    @GET("api/jobs/getjobsformobile")
    suspend fun getJobList(
        @Query("pageno") pageNo: Int,
        @Query("stateid") cityId: String?
    ): Response<ArrayList<Job>>

    @GET("api/application/getapplied")
    suspend fun getApplication(): Response<List<AppliedJob>>

    @GET("api/application/apply")
    suspend fun applyJob(
        @Query("jobid") jobId: String
    ): Response<ArrayList<String>>

    @GET("api/application/getsavedjob")
    suspend fun getSavedJobs(): Response<List<SavedJob>>

    @GET("api/application/save")
    suspend fun saveJob(
        @Query("jobid") jobId: String
    ): Response<ArrayList<String>>

    @GET("api/application/accept")
    suspend fun acceptJob(
        @Query("applicationid") applicationId: String
    ): Response<ArrayList<String>>

    @POST("api/application/withdraw")
    suspend fun withdrawApplication(
        @Body applicationWithdraw: ApplicationWithdraw
    ): Response<ArrayList<String>>

    @GET("api/application/inhandjobs")
    suspend fun getInHandsJobs(): Response<List<Application>>

    @GET("api/application/myoffers")
    suspend fun getMyOffers(): Response<ArrayList<Application>>

    @GET("api/jobs/getjobbyjobid")
    suspend fun getJobDetails(@Query("jobid") jobid: String?,
                              @Query("authToken") authToken: String?): Response<List<JobDetails>>

    @POST("api/jobs/sharejob")
    suspend fun createShareLink(@Body jobDetailsShareLink: JobDetailsShareLink): Response<List<String>>

    @GET("api/application/unsave")
    suspend fun unsaveJob(@Query("saveId")saveId: String): Response<List<String>>

    @GET("api/application/updatestatus")
    suspend fun abandonTask(@Query("applicationid")applicationid: String,
                            @Query("type")type: String,
                            @Query("reason")reason: String):Response<List<String>>

    @GET("api/application/updatestatus")
    suspend fun updateStatusTask(@Query("applicationid")applicationid: String,
                            @Query("type")type: String,
                            @Query("status")status: String):Response<List<String>>

    @GET("api/application/updatestatus")
    suspend fun passTask(@Query("applicationid")applicationid: String,
                                 @Query("type")type: String):Response<List<String>>

    @GET("api/application/getnotes")
    suspend fun getNotes(@Query("applicationid")applicationid: String): Response<List<Notes>>

    @POST("api/application/insertnotes")
    suspend fun submitNotes(@Body notesRequest: NotesRequest): Response<List<String>>

}