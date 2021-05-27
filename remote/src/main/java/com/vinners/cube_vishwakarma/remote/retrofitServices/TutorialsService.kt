package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.help.Help
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse
import retrofit2.Response
import retrofit2.http.GET

interface TutorialsService {

    @GET("api/master/gettutorial?isactive=true")
    suspend fun getTutorialsData():Response<List<TutorialsResponse>>


}