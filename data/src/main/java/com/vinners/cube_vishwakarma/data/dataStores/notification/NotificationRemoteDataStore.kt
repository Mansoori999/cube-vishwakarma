package com.vinners.cube_vishwakarma.data.dataStores.notification

import com.vinners.cube_vishwakarma.data.models.notification.Notification

interface NotificationRemoteDataStore {

    suspend fun getNotification(offset: String): List<Notification>

    suspend fun readNotification(type: String,notificationId: String)

    suspend fun sendDeviceId(token: String?)
}