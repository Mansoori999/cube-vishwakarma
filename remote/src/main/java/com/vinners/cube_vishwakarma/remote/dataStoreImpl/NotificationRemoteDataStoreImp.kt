package com.vinners.cube_vishwakarma.remote.dataStoreImpl

import com.vinners.cube_vishwakarma.data.dataStores.notification.NotificationRemoteDataStore
import com.vinners.cube_vishwakarma.data.models.notification.Notification
import com.vinners.cube_vishwakarma.remote.extensions.bodyOrThrow
import com.vinners.cube_vishwakarma.remote.retrofitServices.NotificationService
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