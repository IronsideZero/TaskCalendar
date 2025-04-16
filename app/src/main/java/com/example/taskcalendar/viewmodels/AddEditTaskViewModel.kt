package com.example.taskcalendar.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskcalendar.data.Graph
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.data.TaskRepo
import com.example.taskcalendar.notifications.TaskNotificationScheduler
import com.example.taskcalendar.notifications.TaskNotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class AddEditTaskViewModel(private val taskRepo: TaskRepo): ViewModel() {

    private val _taskState = MutableStateFlow(Task(0L, "", "", "", "", LocalDate.now(), LocalDateTime.now().plusDays(1), false, false, false, ""))
    val taskState: StateFlow<Task> = _taskState.asStateFlow()

    init {
        Log.d("AddEditTaskViewModel","init called")
        Log.d("AddEditTaskViewModel","taskstate value id is: ${_taskState.value.id}")
        if(_taskState.value.id != 0L) {
            loadTask(_taskState.value.id)
        }
    }

    //mutableStateOf is essentially just the declaration plus the getter
    /*var taskTitleState by mutableStateOf("")
    var taskDescriptionState by mutableStateOf("")
    var taskFrequencyState by mutableStateOf("")
    var taskImplementationWindowState by mutableStateOf("3")
    var taskLastCompletedState by mutableStateOf(LocalDate.now())
    var taskNextScheduledState by mutableStateOf(LocalDateTime.now())
    var taskIsCompletedState by mutableStateOf(false)
    var taskIsMissedState by mutableStateOf(false)
    var taskInProgressState by mutableStateOf(false)
    var taskIsScheduledState by mutableStateOf("Unscheduled")

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
    fun onTaskImplementationWindowChange(newImplementationWindow: String) {
        taskImplementationWindowState = newImplementationWindow
    }
    fun onTaskLastCompletedChange(newLastCompleted: LocalDate) {
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
    fun onTaskIsScheduledChange(newIsScheduled: String) {
        taskIsScheduledState = newIsScheduled
    }*/

    //DB functions
    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.addTask(task = task)
        }
    }

    fun updateTask(context: Context, task:Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.updateTask(task = task)
        }
        /*val notifier = NotificationController(TaskCalendarApp.getContext())
        val scheduler = NotificationScheduler(TaskCalendarApp.getContext())
        if(task.isScheduled == "Scheduled") {
            scheduler.scheduleNotification(500L, "Test notification title", "Test notification message")
        }*/
        //scheduleTaskNotification(context, task)
        scheduleNotificationAlt(context, task)
    }

    fun deleteTask(task:Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepo.deleteTask(task = task)
        }
    }

    fun getTaskByID(id:Long): Flow<Task> {
        return taskRepo.getTaskByID(id)
    }

    fun loadTask(id: Long) {
        Log.d("AddEditTaskViewModel","loadTask called with id ${id}")
        if(id != 0L) {
            viewModelScope.launch {
                /*taskRepo.getTaskByID(id).collect { task ->
                    _taskState.value = task
                }*/
                val task = taskRepo.getTaskByID(id).first()
                _taskState.value = task
                Log.d("AddEditTaskViewModel","loadTask completed, collected task with the following parameters: id=${task.id}, title=${task.title}, desc=${task.description}, frequency=${task.frequency}, window=${task.implementationWindow}, lastCompleted=${task.lastCompletedDate.toString()}, nextScheduled=${task.nextScheduledDateTime.toString()}, isMissed=${task.isMissed},inProgress=${task.isInProgress},scheduledState=${task.isScheduled}")
            }
        } else {
            _taskState.value = Task(0L, "", "", "", "", LocalDate.now(), LocalDateTime.now().plusDays(1), false, false, false, "")
            Log.d("AddEditTaskViewModel","loadTask completed, providing default values")
        }
    }

    fun getAndSetTaskByID(id: Long) {
        /*if (id != 0L) {
            viewModelScope.launch {
                taskRepo.getTaskByID(id).collect { task ->
                    taskTitleState = task.title
                    taskDescriptionState = task.description
                    taskFrequencyState = task.frequency
                    taskImplementationWindowState = task.implementationWindow
                    taskLastCompletedState = task.lastCompletedDate
                    taskNextScheduledState = task.nextScheduledDateTime
                    taskIsCompletedState = task.isCompleted
                    taskIsMissedState = task.isMissed
                    taskInProgressState = task.isInProgress
                    taskIsScheduledState = task.isScheduled
                }
            }
        }*/
        if(id != 0L) {
            viewModelScope.launch {
                taskRepo.getTaskByID(id).collect { task ->
                    _taskState.value = task
                }
            }
        } else {
            _taskState.value = Task(0L, "", "", "", "", LocalDate.now(), LocalDateTime.now().plusDays(1), false, false, false, "")
        }
    }

    fun updateTaskField(update: (Task) -> Task) {
        _taskState.value = update(_taskState.value)
    }

    fun scheduleTaskNotification(context: Context, task: Task) {
        //use current time and scheduled time to determine when the notification should be displayed
        val now = LocalDateTime.now()
        val delay = java.time.Duration.between(now, task.nextScheduledDateTime).toMillis()

        Log.d("AddEditTaskViewModel", "Scheduling notification with a delay of $delay ms")

        if (delay > 0) {
            // Create input data with task details
            val inputData = workDataOf(
                "taskId" to task.id, // Unique ID for each notification
                "taskTitle" to task.title,
                "taskDescription" to task.description,
                "taskImplementationWindow" to task.implementationWindow,
                "taskFrequency" to task.frequency
            )

            // Create a WorkRequest with the input data
            val workRequest = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
                .setInitialDelay(delay, java.util.concurrent.TimeUnit.MILLISECONDS)
                .setInputData(inputData)  // Pass task data to the worker
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d("AddEditTaskViewModel", "Notification scheduled for task: ${task.title}")
        }


    }

    fun scheduleNotificationAlt(context: Context, task: Task, ) {
        //ScheduleNotification().scheduleNotificationWithTask(context, task)
        // Schedule the notification
        //TaskNotificationScheduler.schedule(context, task)
        TaskNotificationScheduler.update(context, task)
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

