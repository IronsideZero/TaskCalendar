package com.example.taskcalendar.notifications

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskcalendar.R
import com.example.taskcalendar.data.NotificationChannels
import kotlin.random.Random


class ReminderReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderReceiver", "onReceive begun")

        //retrieve values from the intent
        val taskId = intent.getStringExtra("id") ?: "-1"
        val title = intent.getStringExtra("title") ?: "Task Reminder"
        val description = intent.getStringExtra("description") ?: "You have a scheduled task."
        val channel = intent.getStringExtra("channel") ?: NotificationChannels.REMINDERS
        val wholeTask = intent.getStringExtra("wholeTask") ?: "No Task Provided"

        //construct the notification
        val notification = NotificationCompat.Builder(context, channel)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()

        //post the notification with a random ID (doesn't need to be random)
        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), notification)
        }
        TaskNotificationScheduler.scheduleNextIfNeeded(context, taskId, wholeTask)
        Log.d("ReminderReceiver", "onReceive ended.")
    }
}

/*
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val title = intent?.getStringExtra("title") ?: "Task Reminder"
        Log.d("ReminderReceiver", "Received Reminder with title: ${title}")
        val notification = NotificationCompat.Builder(context!!, NotificationChannels.DAILY_TASKS)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Reminder")
            .setContentText("It's time for: $title")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}*/
