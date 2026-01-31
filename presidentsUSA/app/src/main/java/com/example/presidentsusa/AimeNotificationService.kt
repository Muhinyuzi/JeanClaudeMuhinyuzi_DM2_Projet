package com.example.presidentsusa

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import kotlin.random.Random


class AimeNotificationService (
    private val context: Context
){
    private val notificationManager=context
        .getSystemService(NotificationManager::class.java)

    fun showNotification(name:String){
        val notification= NotificationCompat
            .Builder(context,"notification_Aime")
            .setContentTitle("Likes des présidents")
            .setContentText("Vous aimez le président ${name}!")
                .setSmallIcon(R.drawable.checkbox)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .build()

                    notificationManager.notify(
                        Random.nextInt(),
                notification
            )
    }
}