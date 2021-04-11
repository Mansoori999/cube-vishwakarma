package com.vinners.trumanms.remote.retrofitServices

import com.vinners.trumanms.data.models.notification.Notification
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {

    @GET("api/notification/get")
    suspend fun getNotification(@Query("offset")offset: String): Response<List<Notification>>

    @GET("api/notification/read")
    suspend fun readNotification(@Query("type")type: String,
                                    @Query("notificationId")notificationId: String): Response<List<String>>

    @GET("api/notification/updatedeviceid")
    suspend fun sendDeviceId(@Query("deviceid")deviceid: String?): Response<List<String>>
}