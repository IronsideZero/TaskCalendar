package com.example.taskcalendar.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.Instant
import java.time.ZoneId

class AlarmController(private val context: Context) {

    fun scheduleAlarm(taskId: Long, title: String, description: String, timeInMillis: Long, channel: String, wholeTask: String) {
        Log.d("AlarmController", "Schedule Alarm Begun")
        //Create an intent that will be recieved by the ReminderReceiver
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("id", taskId.toString())
            putExtra("title", title)
            putExtra("description", description)
            putExtra("channel", channel)
            putExtra("wholeTask", wholeTask)
        }
        Log.d("AlarmController", "An intent has been created with an id of $taskId and a scheduled time of $timeInMillis .")
        val realTime = Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        Log.d("TaskNotificationScheduler", "After reconversion, the scheduled time for ${taskId} is ${realTime}.")
        //
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(), // Unique per task
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        //set the alert and allow it to fire even if the phone is idle or in doze mode
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        Log.d("AlarmController", "Schedule Alarm Ended")
    }

    fun cancelAlarm(taskId: Long) {
        Log.d("AlarmController", "Cancel Alarm Begun")
        val intent = Intent(context, ReminderReceiver::class.java)
        //build the same intent as when the notification was created, so that it can be cancelled.
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            taskId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        //cancel the notification both in the system and in the application's own internal reference
        if (pendingIntent != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
        Log.d("AlarmController", "Cancel Alarm Ended")
    }
}