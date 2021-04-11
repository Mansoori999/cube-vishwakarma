package com.vinners.trumanms.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.vinners.trumanms.R
import com.vinners.trumanms.notification.NotificationChannels.URGENT_NOTIFICATIONS
import com.vinners.trumanms.ui.home.HomeActivity



class NotificationHelper(private val mContext: Context) {

    /**
     * Create and push the notification
     */
    fun createUrgentPriorityNotification(
        title: String,
        message: String,
        pendingIntent: PendingIntent? = null
    ) {

        val finalPendingIntent = if (pendingIntent == null) {
            /**Creates an explicit intent for an Activity in your app */
            val resultIntent = Intent(mContext, HomeActivity::class.java)
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val resultPendingIntent = PendingIntent.getActivity(
                mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            resultPendingIntent
        } else {
            pendingIntent
        }

        val mBuilder = NotificationCompat.Builder(
            mContext,
            NotificationChannels.CHANNEL_URGENT_ID
        )

        mBuilder.setSmallIcon(R.drawable.hd_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ResourcesCompat.getColor(mContext.resources,R.color.button_back,null))
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(finalPendingIntent)

        val mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(URGENT_NOTIFICATIONS)
        }

        mNotificationManager.notify(0 /* Request Code */, mBuilder.build())
    }


    /**
     * Create and push the notification
     */
    fun createNormalPriorityNotification(
        title: String,
        message: String
    ) {
        /**Creates an explicit intent for an Activity in your app */
        val resultIntent = Intent(mContext, HomeActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val resultPendingIntent = PendingIntent.getActivity(
            mContext,
            0 /* Request code */, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val mBuilder = NotificationCompat.Builder(
            mContext,
            NotificationChannels.CHANNEL_URGENT_ID
        )

        mBuilder.setSmallIcon(R.drawable.truman_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ResourcesCompat.getColor(mContext.resources,R.color.colorAccent,null))
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(resultPendingIntent)

        val mNotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(URGENT_NOTIFICATIONS)
        }
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build())
    }
}