package com.example.presidentsusa

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class AimeNotification : Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationChannel= NotificationChannel(
            "notification_Aime",
            "Aime",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}