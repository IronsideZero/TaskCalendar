package com.example.taskcalendar

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.taskcalendar.data.NotificationChannels
import com.example.taskcalendar.nav.Navigation
import com.example.taskcalendar.notifications.NotificationReceiver
import com.example.taskcalendar.ui.theme.TaskCalendarTheme

class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val isPermissionDenied = mutableStateOf(false) // Stores permission status
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register permission request callback
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            isPermissionDenied.value = !isGranted
        }

        createNotificationChannels()

        // Check permission on startup
        checkNotificationPermission()
        PermissionManager.isPermissionDenied = isPermissionDenied
        setContent {
            val isPermissionDeniedPermanently = remember { mutableStateOf(false) }

            TaskCalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val notificationReceiver = NotificationReceiver()
                    val filter = IntentFilter()
                    //this doesn't work because BOOT_COMPLETED is a protected broadcast and won't deliver to a receiver that isn't declared in the manifest. Other intents may go here though
                    //filter.addAction(Intent.ACTION_BOOT_COMPLETED)//this would be good to run to recheck all notifications after a reboot?
                    //this.registerReceiver(notificationReceiver, filter)


                    Navigation()
                }
            }
        }
    }


    //these functions could probably be moved into their own class

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPermission() {
        isPermissionDenied.value = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    //these functions could probably be moved into their own class

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                NotificationChannels.DAILY_TASKS,
                "Daily Tasks",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications are for tasks that will occur every day, or multiple times per week."
            }

            val channel2 = NotificationChannel(
                NotificationChannels.WEEKLY_TASKS,
                "Weekly Tasks",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications are for tasks that will occur once a week, or every other week."
            }

            val channel3 = NotificationChannel(
                NotificationChannels.MONTHLY_TASKS,
                "Monthly Tasks",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications are for tasks that will occur once a month, or every other month."
            }

            val channel4 = NotificationChannel(
                NotificationChannels.LONG_TERM_TASKS,
                "Long Term Tasks",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications are for tasks that will only occur every 3 months or more."
            }

            val channel5 = NotificationChannel(
                NotificationChannels.ONE_TIME_TASKS,
                "One Time Tasks",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications are for tasks that will only occur once."
            }

            val channel6 = NotificationChannel(
                NotificationChannels.REMINDERS,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "These notifications will occur on days 2+ of tasks with multi-day implementation windows."
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannels(listOf(channel1, channel2, channel3, channel4, channel5))
        }
    }


}

