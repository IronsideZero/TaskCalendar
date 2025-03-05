package com.example.taskcalendar.data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var database: TaskDatabase

    val taskRepo by lazy{
        TaskRepo(taskDAO = database.taskDAO())
    }

    fun provide(context: Context) {
        database = Room.databaseBuilder(context, TaskDatabase::class.java, "tasks.db").build()
    }
}