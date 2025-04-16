package com.example.taskcalendar

import android.app.Application
import android.content.Context
import com.example.taskcalendar.data.Graph

class TaskCalendarApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        instance = this
    }

    companion object {
        private lateinit var instance: TaskCalendarApp
        fun getContext(): Context = instance.applicationContext
    }

}