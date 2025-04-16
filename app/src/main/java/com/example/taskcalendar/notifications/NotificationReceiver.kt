package com.example.taskcalendar.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.taskcalendar.R
import com.example.taskcalendar.data.Graph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/*
* This class is a Broadcast Receiver, used to listen for and handle Actions
*
* */
class NotificationReceiver: BroadcastReceiver() {

    private var context: Context? = null
    private lateinit var channel: NotificationChannel
    private var isChannelCreated = false
    private val EVENT_CHANNEL_ID = "EVENT_CHANNEL_ID"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val action = intent?.action

        if (action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("NotificationReceiver", "Reboot completed event received")

            // Optional: Notify user that the app is resetting notifications after boot
            /*NotificationController(context).showNotification(
                title = "Boot Completed",
                message = "Rescheduling notifications..."
            )*/

            // Set up repository and get tasks
            Graph.provide(context)
            val taskRepo = Graph.taskRepo

            CoroutineScope(Dispatchers.Default).launch {
                val tasks = taskRepo.getAllTasks().firstOrNull() ?: return@launch

                TaskNotificationScheduler.rescheduleAfterReboot(context, tasks)

                /*for (task in tasks) {
                    val now = LocalDateTime.now()
                    val scheduledTime = task.nextScheduledDateTime

                    //This is all because the check task.nextScheduledDateTime.isAfter(LocalDateTime.now()) wasn't registering as true even when it was.
                    //Adding this logging spontaneously fixed the problem.
                    //Computer are dumb.
                    Log.d("NotificationReceiver", "Picked up task ${task.title} for time ${task.nextScheduledDateTime}")

                    Log.d("NotificationReceiver", "Now is: $now")
                    Log.d("NotificationReceiver", "Task time is $scheduledTime")
                    var isAfter = scheduledTime.isAfter(now)
                    Log.d("NotificationReceiver", "isAfter is: $isAfter")
                    var comparison = now.compareTo(scheduledTime)
                    Log.d("NotificationReceiver", "Comparing now to scheduledTime is: $comparison where a result of 1 means now is later than scheduledTime, -1 means scheduledTime is later than now.")

                    if (task.nextScheduledDateTime.isAfter(LocalDateTime.now())) {
                        Log.d("NotificationReceiver", "Task is after now")
                    } else {
                        Log.d("NotificationReceiver", "Task is not after now")
                    }

                    Log.d("NotificationReceiver", "Checking task '${task.title}' for rescheduling.")
                    Log.d("NotificationReceiver", "Now: $now, Task Time: $scheduledTime")

                    if (scheduledTime.isAfter(now)) {
                        Log.d("NotificationReceiver", "Rescheduling notification for '${task.title}' at $scheduledTime")
                        TaskNotificationScheduler.schedule(context, task)
                    } else {
                        Log.d("NotificationReceiver", "Skipping task '${task.title}' because it's not in the future.")
                    }
                }*/
            }
        }
    }

    /*override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        val action = intent?.action

        //stuff for alternate notification system
        val scheduleNotificationService = context?.let { Reminder(it) }
        val title: String? = intent?.getStringExtra("Test Title")
        scheduleNotificationService?.sendReminderNotification(title)


        when(action) {
            //This listens for the BOOT_COMPLETED event, which fires when the phone completes a reboot
            Intent.ACTION_BOOT_COMPLETED -> {

                Log.d("NotificationReceiver","Reboot completed event received")
                notifyUser("Notification", "Reboot completed!")

                //use Graph to generate a taskrepo, then get all tasks so that notifications can be rescheduled
                Graph.provide(context!!)

                val workManager = WorkManager.getInstance(context)

                CoroutineScope(Dispatchers.Default).launch {
                    Graph.taskRepo.getAllTasks().firstOrNull()?.let { tasks ->
                        for (task in tasks) {
                            //This is all because the check task.nextScheduledDateTime.isAfter(LocalDateTime.now()) wasn't registering as true even when it was.
                            //Adding this logging spontaneously fixed the problem.
                            //Computer are dumb.
                            Log.d("NotificationReceiver", "Picked up task ${task.title} for time ${task.nextScheduledDateTime}")
                            var now = LocalDateTime.now()
                            var alertTime = task.nextScheduledDateTime
                            Log.d("NotificationReceiver", "Now is: $now")
                            Log.d("NotificationReceiver", "Task time is $alertTime")
                            var isAfter = alertTime.isAfter(now)
                            Log.d("NotificationReceiver", "isAfter is: $isAfter")
                            var comparison = now.compareTo(alertTime)
                            Log.d("NotificationReceiver", "Comparing now to alertTime is: $comparison where a result of 1 means now is later than alertTime, -1 means alertTime is later than now.")
                            if (task.nextScheduledDateTime.isAfter(LocalDateTime.now())) {
                                Log.d("NotificationReceiver", "Task is after now")
                            } else {
                                Log.d("NotificationReceiver", "Task is not after now")
                            }

                            if (task.nextScheduledDateTime.isAfter(LocalDateTime.now())) {
                                val delay = Duration.between(LocalDateTime.now(), task.nextScheduledDateTime).toMillis()
                                Log.d("NotificationReceiver", "Determined that ${task.title} is set for a future time at ${task.nextScheduledDateTime} and must be rescheduled.")
                                // 1. Reschedule WorkManager
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
                                    .build()
                                workManager.enqueue(workRequest)

                                // 2. Reschedule AlarmManager
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
                            }
                        }
                    }
                }
            }
        }
    }*/

    //send an instant notification to the user
    @SuppressLint("MissingPermission")
    fun notifyUser(title:String, text: String) {
        if(!isChannelCreated) {
            createChannel()
        }

        val builder = NotificationCompat.Builder(context!!, EVENT_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(text)
        val notification = builder.build()
        val notificationManagerCompat = NotificationManagerCompat.from(context!!)
        //TODO add permission check
        notificationManagerCompat.notify(1, notification)

    }

    private fun createChannel() {
        channel = NotificationChannel(EVENT_CHANNEL_ID, "TASK_EVENTS", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Main channel for task notifications"
        channel.lightColor = Color.BLUE
        //TODO context!! is a non-null assertion, but an actual null check should really be done here
        val notificationManager = getSystemService(context!!, NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
        isChannelCreated = true
    }
}