package com.vinners.cube_vishwakarma.data.repository

import com.vinners.cube_vishwakarma.data.dataStores.notification.NotificationRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.notification.Notification
import javax.inject.Inject

class NotificationRepositry @Inject constructor(
    private val notificationRemoteDataStore: NotificationRemoteDataStore
) {

    suspend fun getNotification(offset: String): List<Notification>{
        return notificationRemoteDataStore.getNotification(offset)
    }

    suspend fun readNotification(type: String,notificationId: String){
        notificationRemoteDataStore.readNotification(type, notificationId)
    }

    suspend fun sendDeviceId(token: String?){
        notificationRemoteDataStore.sendDeviceId(token)
    }
}