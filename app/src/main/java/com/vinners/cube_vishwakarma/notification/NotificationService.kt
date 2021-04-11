package com.vinners.cube_vishwakarma.notification

import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vinners.cube_vishwakarma.core.base.CoreApplication
import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.data.repository.NotificationRepositry
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import kotlinx.coroutines.*
import javax.inject.Inject


class NotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepositry: NotificationRepositry
    override fun onCreate() {
        super.onCreate()
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)

    }

    fun getCoreComponent(): CoreComponent =
        (this.application as CoreApplication).initOrGetCoreDependency()

    override fun onMessageReceived(message: RemoteMessage) {

        if (message.notification != null) {

            val pendingIntent: PendingIntent? = if (message.notification?.clickAction != null)
                createPendingIntentFromData(message.notification?.clickAction!!, message.data)
            else
                null

            NotificationHelper(applicationContext)
                .createUrgentPriorityNotification(
                    title = message.notification?.title ?: "Truman Gig",
                    message = message.notification?.body ?: "Message",
                    pendingIntent = pendingIntent
                )
        }
    }

    private fun createPendingIntentFromData(
        clickAction: String,
        data: Map<String, String>
    ): PendingIntent {

        TODO()

//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        for (key in data.keys) {
//            intent.putExtra(key, data.get(key))
//        }
//        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                notificationRepositry.sendDeviceId(token)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}