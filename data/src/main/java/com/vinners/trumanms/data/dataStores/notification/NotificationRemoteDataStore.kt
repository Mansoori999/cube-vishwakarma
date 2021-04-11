package com.vinners.trumanms.data.dataStores.notification

import com.vinners.trumanms.data.models.notification.Notification

interface NotificationRemoteDataStore {

    suspend fun getNotification(offset: String): List<Notification>

    suspend fun readNotification(type: String,notificationId: String)

    suspend fun sendDeviceId(token: String?)
}