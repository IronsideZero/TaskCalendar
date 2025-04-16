package com.example.taskcalendar.notifications

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskcalendar.data.NotificationChannels
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.utils.Utilities
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object TaskNotificationScheduler {
    //called to update an existing task. Cancels old notifications and schedules new ones.
    fun update(context: Context, task: Task) {
        cancel(context, task)
        schedule(context, task)
    }
    //called to add a new task, or schedule a notification for the first time for a task
    fun add(context: Context, task: Task) {
        schedule(context, task)
    }
    //called to delete notifications for a task
    fun deleteOnly(context: Context, task: Task) {
        cancel(context, task)
    }

    fun schedule(context: Context, task: Task) {
        val timeInMillis = task.nextScheduledDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        Log.d("TaskNotificationScheduler", "Scheduling task ${task.id} for time ${task.nextScheduledDateTime}.")
        Log.d("TaskNotificationScheduler", "After conversion, the scheduled time for ${task.id} is ${timeInMillis}.")
        val realTime = Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        Log.d("TaskNotificationScheduler", "After reconversion, the scheduled time for ${task.id} is ${realTime}.")
        // Cancel any existing work & alarm
        WorkManager.getInstance(context).cancelAllWorkByTag(task.id.toString())
        AlarmController(context).cancelAlarm(task.id)

        val delay = Duration.between(LocalDateTime.now(), task.nextScheduledDateTime).toMillis()
        Log.d("TaskNotificationScheduler", "The delay for task ${task.id} is ${delay}.")

        val customJsonTask = Utilities.ToJson(task)
        Log.d("TaskNotificationScheduler", "The whole task, after custom jsonification, is ${customJsonTask}")

        //pass details to the WorkManager, which handles background task execution
        val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "taskId" to task.id,
                    "title" to task.title,
                    "description" to task.description,
                    "scheduledTime" to task.nextScheduledDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    "channel" to NotificationChannels.DAILY_TASKS,
                    "wholeTask" to customJsonTask
                )
            )
            .addTag(task.id.toString())
            .build()

        //enqueue the worker to run after the delay
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun scheduleNextIfNeeded(context: Context, taskId: String, wholeTask: String) {
        Log.d("TaskNotificationScheduler", "Checking if task $taskId needs to have another notification scheduled.")
        Log.d("TaskNotificationScheduler", "Provided whole task is: $wholeTask")

        //val task = Gson().fromJson(wholeTask, Task::class.java)
        val task = Utilities.FromJson(wholeTask)
        Log.d("TaskNotificationScheduler", "The whole task, after conversion from json, is ${task}")

        //nextScheduledDateTime indicates when the task started. implementationWindow indicates when it ends. frequency indicates how many notifications should be sent, given the window.
        //figure out if current date is within the window
        //figure out if the time of next notification (given frequency) would be within the window
        //if so, schedule a new notification (don't change nextScheduledDateTime

        val currentDate = LocalDateTime.now()
        val window = task.implementationWindow.toLong()
        val endOfWindow = currentDate.plusDays(window).minusMinutes(1)
        val nextNotificationTime = task.nextScheduledDateTime.plusDays(1)

        Log.d("TaskNotificationScheduler", "Current date is $currentDate")
        Log.d("TaskNotificationScheduler", "Implementation window is $window")
        Log.d("TaskNotificationScheduler", "End of window is $endOfWindow")
        Log.d("TaskNotificationScheduler", "Next notification would be at $nextNotificationTime")
        //Log.d("TaskNotificationScheduler", "")

        val taskStillInWindow = endOfWindow.isAfter(currentDate)
        val nextNotificationStillInWindow = endOfWindow.isAfter(nextNotificationTime)


        if(taskStillInWindow && nextNotificationStillInWindow) {
            Log.d("TaskNotificationScheduler", "The task is still within the implementation window, another notification will be scheduled.")
            //update the task nextScheduledDateTime to be same time tomorrow. Note: This will not change the value stored in the DB, this is a temporary reassignment in order to allow the notification to be scheduled
            task.nextScheduledDateTime = task.nextScheduledDateTime.plusDays(1)
            //task.nextScheduledDateTime = task.nextScheduledDateTime.plusMinutes(3)
            Log.d("TaskNotificationScheduler", "Scheduling another notification for task ${task.id} at ${task.nextScheduledDateTime}.")
            schedule(context, task)
        } else if(taskStillInWindow && !nextNotificationStillInWindow) {
            Log.d("TaskNotificationScheduler", "The implementation window is not yet complete, however it is the last day of the window. A new notification will not be scheduled.")
        } else {
            Log.d("TaskNotificationScheduler", "Task complete. A new notification will not be scheduled.")
        }

    }

    fun rescheduleAfterReboot(context: Context, allTasks: List<Task>) {
        //taken from previous implementation in NotificationReceiver. Addresses future tasks only, not any task mid-implementation
        for (task in allTasks) {
            val now = LocalDateTime.now()
            val scheduledTime = task.nextScheduledDateTime

            //This is all because the check task.nextScheduledDateTime.isAfter(LocalDateTime.now()) wasn't registering as true even when it was.
            //Adding this logging spontaneously fixed the problem.
            //Computer are dumb.
            Log.d("TaskNotificationScheduler", "Picked up task ${task.title} for time ${task.nextScheduledDateTime}")

            Log.d("TaskNotificationScheduler", "Now is: $now")
            Log.d("TaskNotificationScheduler", "Task time is $scheduledTime")
            var isAfter = scheduledTime.isAfter(now)
            Log.d("TaskNotificationScheduler", "isAfter is: $isAfter")
            var comparison = now.compareTo(scheduledTime)
            Log.d("TaskNotificationScheduler", "Comparing now to scheduledTime is: $comparison where a result of 1 means now is later than scheduledTime, -1 means scheduledTime is later than now.")

            if (task.nextScheduledDateTime.isAfter(LocalDateTime.now())) {
                Log.d("TaskNotificationScheduler", "Task is after now")
            } else {
                Log.d("TaskNotificationScheduler", "Task is not after now")
            }

            Log.d("TaskNotificationScheduler", "Checking task '${task.title}' for rescheduling.")
            Log.d("TaskNotificationScheduler", "Now: $now, Task Time: $scheduledTime")

            if (scheduledTime.isAfter(now)) {
                Log.d("TaskNotificationScheduler", "Rescheduling notification for '${task.title}' at $scheduledTime")
                TaskNotificationScheduler.schedule(context, task)
            } else {
                Log.d("TaskNotificationScheduler", "Skipping task '${task.title}' because it's not in the future.")
            }
        }
    }
    /*private fun schedule(context: Context, task: Task) {
        Log.d("AddEditTaskViewModel", "TaskNotificationScheduler.schedule fired for task: ${task.title} at ${task.nextScheduledDateTime}")
        val now = LocalDateTime.now()
        if (task.nextScheduledDateTime.isBefore(now)) return

        val delay = Duration.between(now, task.nextScheduledDateTime).toMillis()

        // 1. WorkManager (resilient, survives reboot)
        val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "taskId" to task.id,
                    "title" to task.title,
                    "description" to task.description,
                    "taskImplementationWindow" to task.implementationWindow,
                    "taskFrequency" to task.frequency
                )
            )
            .addTag(task.id.toString())
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)

        // 2. AlarmManager (precise)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("taskId", task.id)
            putExtra("title", task.title)
            putExtra("description", task.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(), // unique request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = task.nextScheduledDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }*/
    private fun cancel(context: Context, task: Task) {
        /*// Cancel AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        // Cancel WorkManager
        WorkManager.getInstance(context).cancelAllWorkByTag(task.id.toString())*/

        WorkManager.getInstance(context).cancelAllWorkByTag(task.id.toString())
        AlarmController(context).cancelAlarm(task.id)
    }
}