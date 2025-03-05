package com.example.taskcalendar.data

import kotlinx.coroutines.flow.Flow

class TaskRepo(private val taskDAO: TaskDAO) {

    suspend fun addTask(task:Task) {
        taskDAO.addTask(task)
    }

    fun getAllTasks() :Flow<List<Task>> = taskDAO.getAllTasks()

    fun getTaskByID(id: Long): Flow<Task> {
        return taskDAO.getTaskById(id)
    }

    suspend fun updateTask(task:Task) {
        taskDAO.updateTask(task)
    }

    suspend fun deleteTask(task:Task) {
        taskDAO.deleteTask(task)
    }

    //TODO get tasks by date range
    //TODO get tasks by completion status
    //TODO get tasks by scheduled status
}