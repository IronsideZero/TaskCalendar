package com.example.taskcalendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.TaskRepo

class TaskListViewModel(private val taskRepo: TaskRepo): ViewModel() {





    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val repo = Graph.taskRepo
                return TaskListViewModel(repo) as T
            }
        }
    }
}