package com.example.taskcalendar.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskcalendar.data.NotificationChannels
import java.time.Instant
import java.time.ZoneId

/*
This class handles scheduling notifications. It runs in the background, and notifications that it posts go to the android OS, meaning they will fire at the proper time even if the app isn't running.
* */

class TaskNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("TaskNotificationWorker", "Work begun.")
        val taskId = inputData.getLong("taskId", -1L)
        val title = inputData.getString("title") ?: return Result.failure()
        val description = inputData.getString("description") ?: ""
        val scheduledTime = inputData.getLong("scheduledTime", -1L)
        val channel = inputData.getString("channel") ?: NotificationChannels.DAILY_TASKS
        val wholeTask = inputData.getString("wholeTask") ?: "No Task Provided"

        //check to ensure minimum data is present
        if (taskId == -1L || scheduledTime == -1L) return Result.failure()

        Log.d("TaskNotificationWorker", "Work has begun on scheduling a notification for a task with an id of $taskId and a scheduled time of $scheduledTime")
        val realTime = Instant.ofEpochMilli(scheduledTime).atZone(ZoneId.systemDefault()).toLocalDateTime()
        Log.d("TaskNotificationScheduler", "After reconversion, the scheduled time for ${taskId} is ${realTime}.")

        // Schedule exact alarm
        AlarmController(applicationContext).scheduleAlarm(
            taskId,
            title,
            description,
            scheduledTime,
            channel,
            wholeTask
        )
        Log.d("TaskNotificationWorker", "Work ended.")
        return Result.success()
    }
}


/*
class TaskNotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve task data
        val taskTitle = inputData.getString("taskTitle") ?: "Task Reminder"
        val taskDescription = inputData.getString("taskDescription") ?: "You have a scheduled task."
        val taskId = inputData.getInt("taskId", 0)  // Default to 0 if not provided
        val taskWindow = inputData.getString("taskImplementationWindow") ?: "1"
        val taskFrequency = inputData.getString("taskFrequency") ?: "Daily"

        Log.d("TaskNotificationWorker", "Doing work with $taskTitle")

        //based on task frequency, select a notification channel
        //"Once Only", "Every Day", "Every Week", "Twice A Month", "Every Month", "Every 2 Months", "Every 3 Months", "Twice A Year", "Every Year"

        //based on task implementation window, determine how many notifications to schedule (generally 1 per day of the window at the same time)

        showNotification(taskId, taskTitle, taskDescription, NotificationChannels.DAILY_TASKS)
        return Result.success()
    }


    private fun showNotification(taskId: Int, taskTitle: String, taskDescription: String, channel: String) {
        val channelId = "TASK_NOTIFICATION_CHANNEL"
        Log.d("TaskNotificationWorker", "showNotification with $taskTitle")
        //check that we have permission before proceeding
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return // Permission is not granted, so we do not post the notification
            }
        }

        // Build the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(taskTitle)
            .setContentText(taskDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        //show the notification. In AddEditTaskViewModel.showNotification, we set up a delay. I'm not sure how the delay makes it here, but it does.
        //I'm not sure that this is the best option for scheduling notifications in the long term though, this seems more like an immediate or nearly immediate notification sort of thing.
        with(NotificationManagerCompat.from(context)) {
            notify(taskId, builder.build())
        }
    }


}*/
