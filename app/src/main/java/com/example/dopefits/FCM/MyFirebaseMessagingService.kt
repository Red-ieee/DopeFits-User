package com.example.dopefits.com.example.dopefits.FCM

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dopefits.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private fun sendNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "Your_Channel_ID"
            val channelName = "Your Channel Name"
            val channelDescription = "Channel Description"
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notificationId = Random.nextInt() // Random ID for notifications
        val builder = NotificationCompat.Builder(this, "Your_Channel_ID")
            .setSmallIcon(R.drawable.ic_notification) // your notification icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        notificationManager.notify(notificationId, builder.build())
    }
}
