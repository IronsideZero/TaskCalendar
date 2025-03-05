package com.example.taskcalendar.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.data.TaskRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AddEditTaskViewModel(private val taskRepo: TaskRepo): ViewModel() {

    //mutableStateOf is essentially just the declaration plus the getter
    var taskTitleState by mutableStateOf("")
    var taskDescriptionState by mutableStateOf("")
    var taskFrequencyState by mutableStateOf("")
    var taskImplementationWindowState by mutableStateOf(3)
    var taskLastCompletedState by mutableStateOf(LocalDateTime.now())
    var taskNextScheduledState by mutableStateOf(LocalDateTime.now())
    var taskIsCompletedState by mutableStateOf(false)
    var taskIsMissedState by mutableStateOf(false)
    var taskInProgressState by mutableStateOf(false)
    var taskIsScheduledState by mutableStateOf(false)

    //setters
    fun onTaskTitleChange(newTitle: String) {
        taskTitleState = newTitle
    }
    fun onTaskDescriptionChange(newDescription: String) {
        taskDescriptionState = newDescription
    }
    fun onTaskFrequencyChange(newFrequency: String) {
        taskFrequencyState = newFrequency
    }
    fun onTaskImplementationWindowChange(newImplementationWindow: Int) {
        taskImplementationWindowState = newImplementationWindow
    }
    fun onTaskLastCompletedChange(newLastCompleted: LocalDateTime) {
        taskLastCompletedState = newLastCompleted
    }
    fun onTaskNextScheduledChange(newNextScheduled: LocalDateTime) {
        taskNextScheduledState = newNextScheduled
    }
    fun onTaskIsCompletedChange(newIsCompleted: Boolean) {
        taskIsCompletedState = newIsCompleted
    }
    fun onTaskIsMissedChange(newIsMissed: Boolean) {
        taskIsMissedState = newIsMissed
    }
    fun onTaskInProgressChange(newInProgress: Boolean) {
        taskInProgressState = newInProgress
    }
    fun onTaskIsScheduledChange(newIsScheduled: Boolean) {
        taskIsScheduledState = newIsScheduled
    }

    //DB functions
    fun addWish(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(task = task)
        }
    }

    fun updateWish(task:Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.updateTask(task = task)
        }
    }

    fun deleteWish(task:Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.deleteTask(task = task)
        }
    }

    fun getAWishById(id:Long): Flow<Task> {
        return taskRepo.getTaskByID(id)
    }






    //viewmodel factory
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val repo = Graph.taskRepo
                return AddEditTaskViewModel(repo) as T
            }
        }
    }
}