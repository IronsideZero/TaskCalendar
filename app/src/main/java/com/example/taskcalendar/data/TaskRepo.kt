package com.example.taskcalendar.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime

class TaskRepo(private val taskDAO: TaskDAO) {

    suspend fun addTask(task:Task) {
        taskDAO.addTask(task)
    }

    fun getAllTasks() :Flow<List<Task>> = taskDAO.getAllTasks()

    fun getTaskByID(id: Long): Flow<Task> {
        return taskDAO.getTaskById(id)
            .map { it ?: Task(0L, "", "", "", "", LocalDate.now(), LocalDateTime.now().plusDays(1), false, false, false, "")}
    }

    suspend fun updateTask(task:Task) {
        runBlocking {
            taskDAO.updateTask(task)
            delay(200)
        }

    }

    suspend fun deleteTask(task:Task) {
        taskDAO.deleteTask(task)
    }

    //TODO get tasks by date range
    //TODO get tasks by completion status
    //TODO get tasks by scheduled status
}