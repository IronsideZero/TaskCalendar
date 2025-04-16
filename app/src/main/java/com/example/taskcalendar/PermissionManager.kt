package com.example.taskcalendar

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
/*
* This class keeps track of permissions the app uses and facilitates the user granting them
* */
object PermissionManager {
    var isPermissionDenied: MutableState<Boolean>? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    }

    //opens app settings to the section for this app
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK // Required when launching from a non-Activity context
        }
        context.startActivity(intent)
    }
}