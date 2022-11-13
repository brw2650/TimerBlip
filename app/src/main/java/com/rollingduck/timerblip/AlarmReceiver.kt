package com.rollingduck.timerblip

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    private val channelId = "notificationChannel"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm triggered")
        vibrate(context)
    }

    private fun vibrate(context: Context?) {
        if (context == null) {
            Log.e("AlarmReceiver", "No context")
            return
        }

        createNotificationChannel(context)
        createNotification(context)
        Log.d("AlarmReceiver", "Vibration!")

        AlarmController.setAlarm(context)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "TimerBlip"
        val descriptionText = "NotificationChannel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager?.createNotificationChannel(channel)
    }

    private fun createNotification(context: Context) {
        val notificationId = 123
        val now = Calendar.getInstance()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Chime")
            .setContentText("It's ${now.time}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setTimeoutAfter(5000)
            .setCategory(Notification.CATEGORY_ALARM)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}