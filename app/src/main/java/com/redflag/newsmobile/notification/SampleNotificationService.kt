package com.redflag.newsmobile.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class SampleNotificationService(
    private val context: Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun showBasicNotification() {
        val notification = NotificationCompat.Builder(context, "base_notification")
            .setContentTitle("Base notification")
            .setContentText("Catalogo criado com sucesso")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}