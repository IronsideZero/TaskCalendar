package com.example.taskcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.data.TaskRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskController(val taskRepo: TaskRepo = Graph.taskRepo): ViewModel() {
        lateinit var getAllTasks: Flow<List<Task>>

        init {
            viewModelScope.launch {
                getAllTasks = taskRepo.getAllTasks()
            }
        }

    //val notifier = NotificationController(TaskCalendarApp.getContext())
    //val scheduler = NotificationScheduler(TaskCalendarApp.getContext())

    fun getTaskByID(id: Long):Flow<Task> {
        return taskRepo.getTaskByID(id)
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(task)
        }
        if(task.isScheduled == "Scheduled") {
            //scheduler.scheduleNotification(500L, "Test notification title", "Test notification message")
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.updateTask(task)
        }
        if(task.isScheduled == "Scheduled") {
            //scheduler.scheduleNotification(500L, "Test notification title", "Test notification message")
        }
    }

    fun setNotification(task: Task) {
        if(task.isScheduled == "Scheduled") {
            //scheduler.scheduleNotification(500L, "Test notification title", "Test notification message")
        }
    }


    //TODO get all tasks
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
}

