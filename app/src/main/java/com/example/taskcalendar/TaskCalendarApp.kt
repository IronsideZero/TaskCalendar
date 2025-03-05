package com.example.taskcalendar

import android.app.Application
import com.example.taskcalendar.data.Graph

class TaskCalendarApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}