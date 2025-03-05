package com.example.taskcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.data.TaskRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow

class TaskController(val taskRepo: TaskRepo = Graph.taskRepo): ViewModel() {
        lateinit var getAllTasks: Flow<List<Task>>

        init {
            viewModelScope.launch {
                getAllTasks = taskRepo.getAllTasks()
            }
        }

    fun getTaskByID(id: Long):Flow<Task> {
        return taskRepo.getTaskByID(id)
    }


    //TODO add task
    //TODO edit task
    //TODO get all tasks
    //TODO
    //TODO
    //TODO
    //TODO
    //TODO
}

