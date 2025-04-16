package com.example.taskcalendar.notifications

import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.taskcalendar.R
import com.example.taskcalendar.data.NotificationChannels

class Reminder(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    fun sendReminderNotification(title: String?) {
        Log.d("Reminder.kt", "Setting reminder in Reminder class")
        val notification = NotificationCompat.Builder(context, NotificationChannels.DAILY_TASKS)
            .setContentText(context.getString(R.string.app_name))
            .setContentTitle(title)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setLargeIcon(
                BitmapFactory.decodeResource(context.resources,
                    R.drawable.baseline_notifications_24
            ))
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("It's time for $title")
            )
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(context.bitmapFromResource(R.drawable.baseline_notifications_24))
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun Context.bitmapFromResource(
        @DrawableRes resId: Int
    ) = BitmapFactory.decodeResource(
        resources,
        resId
    )
}