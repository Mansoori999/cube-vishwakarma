package com.vinners.trumanms.notification

import android.app.PendingIntent
import android.content.Intent
import android.widget.Toast
import com.example.feature_profile.ui.jobHistory.JobHistoryActivity
import com.example.notification.ui.NotificationActivity
import com.google.android.gms.common.api.Api
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vinners.trumanms.core.base.CoreApplication
import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.data.repository.NotificationRepositry
import com.vinners.trumanms.di.DaggerLauncherComponent
import com.vinners.trumanms.ui.home.HomeActivity
import dagger.android.AndroidInjection
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

        val intent =
            when (clickAction) {
            NotificationActions.ACTION_OPEN_HOME -> {
                Intent(
                    applicationContext,
                    HomeActivity::class.java
                )
            }
            NotificationActions.ACTION_OPEN_TASK_HISTORY -> {
                Intent(
                    applicationContext,
                    JobHistoryActivity::class.java
                )
            }
            NotificationActions.ACTION_OPEN_TASK_IN_HAND -> {
                Intent(
                    applicationContext,
                    HomeActivity::class.java
                )
            }
                NotificationActions.ACTION_ACCEPT_MY_OFFERS -> {
                    Intent(
                        applicationContext,
                        HomeActivity::class.java
                    )
                }

                else -> {
                    Intent(applicationContext, NotificationActivity::class.java)
                }
            }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    for (key in data.keys) {
                    intent.putExtra(key, data.get(key))
                }
                return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            }

        override  fun onNewToken(token: String) {
          CoroutineScope(Dispatchers.IO).launch {
              try {
                  notificationRepositry.sendDeviceId(token)
              }catch (e: Exception){
                 e.printStackTrace()
              }
          }
        }


    }