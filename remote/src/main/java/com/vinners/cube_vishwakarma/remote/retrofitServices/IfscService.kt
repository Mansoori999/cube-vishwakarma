package com.vinners.cube_vishwakarma.remote.retrofitServices

import com.vinners.cube_vishwakarma.data.models.bank.BankDetails
import retrofit2.Response
import retrofit2.http.*

interface IfscService {
    @GET("{ifcsCode}")
    suspend fun getBankDetails(@Path("ifcsCode") ifcsCode: String?): Response<BankDetails>


}