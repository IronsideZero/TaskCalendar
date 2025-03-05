package com.example.taskcalendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.taskcalendar.TaskController
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.TaskRepo

class MainViewModel(private val taskRepo: TaskRepo): ViewModel() {





    //a companion object is for things that are directly related to a class, but do not depend on a specific instance of that class
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val repo = Graph.taskRepo
                return MainViewModel(repo) as T
            }
        }
    }
}

