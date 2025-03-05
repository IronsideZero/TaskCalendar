package com.example.taskcalendar

import androidx.lifecycle.ViewModel
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.TaskRepo

class TaskViewModel(private val taskRepo: TaskRepo = Graph.taskRepo): ViewModel() {

}