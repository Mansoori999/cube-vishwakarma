package com.vinners.trumanms.remote.dataStoreImpl

import com.vinners.trumanms.data.dataStores.notification.NotificationRemoteDataStore
import com.vinners.trumanms.data.models.notification.Notification
import com.vinners.trumanms.remote.extensions.bodyOrThrow
import com.vinners.trumanms.remote.retrofitServices.NotificationService
import javax.inject.Inject

class NotificationRemoteDataStoreImp @Inject constructor(
    private val notificationService: NotificationService
): NotificationRemoteDataStore {

    override suspend fun getNotification(offset: String): List<Notification> {
        return notificationService.getNotification(offset).bodyOrThrow()
    }

    override suspend fun readNotification(type: String, notificationId: String) {
        notificationService.readNotification(type, notificationId)
    }

    override suspend fun sendDeviceId(token: String?) {
        notificationService.sendDeviceId(token).bodyOrThrow()
    }
}