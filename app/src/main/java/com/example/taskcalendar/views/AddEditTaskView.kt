package com.example.taskcalendar.views

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.taskcalendar.PermissionManager
import com.example.taskcalendar.PermissionManager.openAppSettings
import com.example.taskcalendar.R
import com.example.taskcalendar.TaskController
import com.example.taskcalendar.utils.CheckboxSelector
import com.example.taskcalendar.utils.DatePickerPopup
import com.example.taskcalendar.utils.DropDownSelector2
import com.example.taskcalendar.utils.TaskDatePickerButton
import com.example.taskcalendar.utils.TaskTextField
import com.example.taskcalendar.utils.TaskTimePickerButton
import com.example.taskcalendar.utils.TimePickerPopup
import com.example.taskcalendar.viewmodels.AddEditTaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskView(
    id: Long,
    controller: TaskController,
    viewModel: AddEditTaskViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val snackMessage = remember{
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    //scaffoldState is used to update the contents of the scaffold immediately upon change
    val scaffoldState = rememberScaffoldState()

    val task by viewModel.taskState.collectAsState()
    //val taskState by viewModel.taskState.collectAsState()
    //val task by rememberUpdatedState(taskState)

    var isPermissionDenied = PermissionManager.isPermissionDenied?.value ?: false

    if(id != 0L) {
        //viewModel.getAndSetTaskByID(id)

        //val task = controller.getTaskByID(id).collectAsState(initial = Task(0L, "", ""))
        //changing these values directly? Feels like we should be using viewModel.onWishTitleChanged(wish.value.title) like we do below, since that's the whole point of having these methods

        //viewModel.taskTitleState = task.value.title
        //viewModel.taskDescriptionState = task.value.description
        //viewModel.taskFrequencyState = task.value.frequency
        //viewModel.taskImplementationWindowState = task.value.implementationWindow
        //viewModel.taskLastCompletedState = task.value.lastCompletedDate
        //viewModel.taskNextScheduledState = task.value.nextScheduledDateTime
        //viewModel.taskIsCompletedState = task.value.isCompleted
        //viewModel.taskIsMissedState = task.value.isMissed
        //viewModel.taskInProgressState = task.value.isInProgress
        //viewModel.taskIsScheduledState = task.value.isScheduled
    } else {
        /*viewModel.taskTitleState = ""
        viewModel.taskDescriptionState = ""
        viewModel.taskFrequencyState = ""
        viewModel.taskImplementationWindowState = "1"
        viewModel.taskLastCompletedState = null
        viewModel.taskNextScheduledState = null
        viewModel.taskIsCompletedState = false
        viewModel.taskIsMissedState = false
        viewModel.taskInProgressState = false
        viewModel.taskIsScheduledState = "Unscheduled"*/
        //viewModel.getAndSetTaskByID(id)
    }

    //states for the date and time pickers
    var showLastDatePicker by remember { mutableStateOf(false)}
    //var selectedLastImplementedDate by remember { mutableStateOf<LocalDate?>(if(viewModel.taskLastCompletedState != null) viewModel.taskLastCompletedState else null)}
    //var selectedLastImplementedDate by remember { mutableStateOf<LocalDate?>(if(task.lastCompletedDate != null) task.lastCompletedDate else null)}

    var showNextDatePicker by remember { mutableStateOf(false)}
    //var selectedNextScheduledDate by remember { mutableStateOf<LocalDate?>(if(viewModel.taskNextScheduledState != null) viewModel.taskNextScheduledState.toLocalDate() else null)}
    //var selectedNextScheduledDate by remember { mutableStateOf<LocalDate?>(if(task.nextScheduledDateTime != null) task.nextScheduledDateTime.toLocalDate() else null)}

    var showNextTimePicker by remember { mutableStateOf(false)}
    //var selectedNextScheduledTime by remember {mutableStateOf<LocalTime?>(if(viewModel.taskNextScheduledState != null) viewModel.taskNextScheduledState.toLocalTime() else null)}
    //var selectedNextScheduledTime by remember {mutableStateOf<LocalTime?>(if(task.nextScheduledDateTime != null) task.nextScheduledDateTime.toLocalTime() else null)}


    //states for the dropdowns
    //var taskFrequencyDropdownSelection by remember { mutableStateOf(task.frequency?: "Implementation Window..")}
    //var taskImplementationWindowSelection by remember { mutableStateOf(task.implementationWindow ?: "1")}
    //var taskIsScheduledSelection by remember { mutableStateOf(task.isScheduled ?: "Unscheduled")}

    //dropdown options
    var frequencyOptions = listOf("Once Only", "Every Day", "Every Week", "Twice A Month", "Every Month", "Every 2 Months", "Every 3 Months", "Twice A Year", "Every Year")
    var implementationWindowOptions = listOf("1", "2", "3", "4", "5", "6", "7", "Unlimited")
    var scheduleOptions = listOf("Scheduled", "Unscheduled", "Misc. To-Do")

    //checkbox states
    //var taskIsCompletedChecked by remember { mutableStateOf(task.isCompleted?: false)}
    //var taskIsMissedChecked by remember { mutableStateOf(task.isMissed?: false)}
    //var taskIsInProgressChecked by remember { mutableStateOf(task.isInProgress?: false)}

    LaunchedEffect(id) {
        Log.d("AddEditTaskView", "Executing LaunchedEffect with id $id")

        //viewModel.getAndSetTaskByID(id)
        viewModel.loadTask(id)
        /*selectedLastImplementedDate = task.lastCompletedDate
        selectedNextScheduledDate = task.nextScheduledDateTime.toLocalDate()
        selectedNextScheduledTime = task.nextScheduledDateTime.toLocalTime()
        taskFrequencyDropdownSelection = task.frequency
        taskImplementationWindowSelection =task.implementationWindow
        taskIsScheduledSelection =task.isScheduled
        taskIsCompletedChecked =task.isCompleted
        taskIsMissedChecked =task.isMissed
        taskIsInProgressChecked =task.isInProgress*/
        Log.d("AddEditTaskView", "LaunchedEffect completed.")
        Log.d("AddEditTaskView", "Task is now: id=${task.id}, title=${task.title}, desc=${task.description}, frequency=${task.frequency}, window=${task.implementationWindow}, lastCompleted=${task.lastCompletedDate.toString()}, nextScheduled=${task.nextScheduledDateTime.toString()}, isMissed=${task.isMissed},inProgress=${task.isInProgress},scheduledState=${task.isScheduled}")

    }

    // Observe lifecycle to refresh permission state
    //this triggers when the app resumes from a paused state, like when the user goes to settings. It checks and refreshes the permission state, which determines whether or not to display the warning message
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onResume(owner: LifecycleOwner) {
                isPermissionDenied = PermissionManager.checkPermission(context)
            }
        })
    }

    var selectedLastImplementedDate = task.lastCompletedDate
    var selectedNextScheduledDate = task.nextScheduledDateTime.toLocalDate()
    var selectedNextScheduledTime = task.nextScheduledDateTime.toLocalTime()
    var taskFrequencyDropdownSelection = task.frequency
    var taskImplementationWindowSelection = task.implementationWindow
    var taskIsScheduledSelection = task.isScheduled
    var taskIsCompletedChecked = task.isCompleted
    var taskIsMissedChecked = task.isMissed
    var taskIsInProgressChecked = task.isInProgress

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            //TASK TITLE
            TaskTextField(label = "Title", value = task.title) {
                /*viewModel.onTaskTitleChange(
                    it
                )*/
                newTitle ->
                    viewModel.updateTaskField { it.copy(title = newTitle) }
            }
            Spacer(modifier = Modifier.height(10.dp))
            //TASK DESCRIPTION
            TaskTextField(label = "Description", value = task.description) {
                /*viewModel.onTaskDescriptionChange(
                    it
                )*/
                newDescription ->
                    viewModel.updateTaskField { it.copy(description = newDescription) }
            }
            Spacer(modifier = Modifier.height(10.dp))
            //TASK FREQUENCY
            DropDownSelector2(
                label = "Frequency",
                selectedValue = task.frequency,
                options = frequencyOptions,
                onItemSelected = {selectedFrequency ->
                    viewModel.updateTaskField { it.copy(frequency = selectedFrequency) }
                })
            Spacer(modifier = Modifier.height(10.dp))
            //TASK IMPLEMENTATION WINDOW
            DropDownSelector2(
                label = "Implementation Window",
                selectedValue = task.implementationWindow,
                options = implementationWindowOptions,
                onItemSelected = { selectedWindow ->
                    viewModel.updateTaskField { it.copy(implementationWindow = selectedWindow) }
                })
            Spacer(modifier = Modifier.height(10.dp))
            //TASK LAST COMPLETED DATE
            TaskDatePickerButton(
                label = "Last Completed Date",
                value = selectedLastImplementedDate?.let {selectedLastImplementedDate.toString()} ?: "",
                onClick = { showLastDatePicker = true }
            )
            if(showLastDatePicker) {
                DatePickerPopup(
                    onDateSelected = {selectedDate ->
                        //selectedLastImplementedDate = it
                        //viewModel.taskLastCompletedState = selectedLastImplementedDate
                        viewModel.updateTaskField {
                            if (selectedDate != null) {
                                it.copy(lastCompletedDate = selectedDate)
                            } else {
                                it.copy(lastCompletedDate = LocalDate.now())
                            }
                        }
                        showLastDatePicker = false
                    },
                    onDismiss = { showLastDatePicker = false}
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            //TASK NEXT SCHEDULED DATE AND TIME
            TaskDatePickerButton(
                label = "Next Scheduled Date",
                value = selectedNextScheduledDate?.let {selectedNextScheduledDate.toString()} ?: "",
                onClick = { showNextDatePicker = true }
            )
            if(showNextDatePicker) {
                DatePickerPopup(
                    onDateSelected = {selectedDate ->
                        //selectedNextScheduledDate = it
                        /*if(selectedNextScheduledDate != null && selectedNextScheduledTime != null) {
                            viewModel.taskNextScheduledState = selectedNextScheduledDate!!.atTime(selectedNextScheduledTime)
                        }*/
                        if(selectedDate != null) {
                            selectedNextScheduledDate = selectedDate
                            if(selectedNextScheduledTime != null) {
                                viewModel.updateTaskField { it.copy(nextScheduledDateTime = selectedNextScheduledDate!!.atTime(selectedNextScheduledTime)) }
                            }
                        }

                        showNextDatePicker = false
                    },
                    onDismiss = { showNextDatePicker = false}
                )
            }
            TaskTimePickerButton(
                label = "Next Scheduled Time",
                value = selectedNextScheduledTime?.let {selectedNextScheduledTime.toString()} ?: "",
                onClick = { showNextTimePicker = true}
            )
            if(showNextTimePicker) {
                TimePickerPopup(
                    onConfirm = {selectedTime ->
                        selectedNextScheduledTime = LocalTime.of(selectedTime.hour, selectedTime.minute)
                        if(selectedNextScheduledDate != null && selectedNextScheduledTime != null) {
                            //viewModel.taskNextScheduledState = selectedNextScheduledDate!!.atTime(selectedNextScheduledTime)
                            viewModel.updateTaskField { it.copy(nextScheduledDateTime = selectedNextScheduledDate!!.atTime(selectedNextScheduledTime)) }
                        }
                        showNextTimePicker = false
                    },
                    onDismiss = { showNextTimePicker = false}
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            //TASK IS COMPLETED CHECKBOX
            CheckboxSelector(
                label = "Task Completed",
                checked = task.isCompleted,
                onCheckedChange = {
                    //taskIsCompletedChecked = it
                    //viewModel.taskIsCompletedState = taskIsCompletedChecked
                    isCompleted ->
                        viewModel.updateTaskField { it.copy(isCompleted = isCompleted) }
                })
            Spacer(modifier = Modifier.height(10.dp))
            //TASK IS MISSED CHECKBOX ??
            CheckboxSelector(
                label = "Task Missed",
                checked = task.isMissed,
                onCheckedChange = {
                    //taskIsMissedChecked = it
                    //viewModel.taskIsMissedState = taskIsMissedChecked
                    isMissed ->
                        viewModel.updateTaskField { it.copy(isMissed = isMissed) }
                })
            Spacer(modifier = Modifier.height(10.dp))
            //TASK IS IN PROGRESS CHECKBOX
            CheckboxSelector(
                label = "Task In Progress",
                checked = task.isInProgress,
                onCheckedChange = {
                    //taskIsInProgressChecked = it
                    //viewModel.taskInProgressState = taskIsInProgressChecked
                    isInProgress ->
                        viewModel.updateTaskField { it.copy(isInProgress = isInProgress) }
                })
            Spacer(modifier = Modifier.height(10.dp))
            //TASK IS SCHEDULED
            DropDownSelector2(
                label = "Scheduled State",
                selectedValue = task.isScheduled,
                options = scheduleOptions,
                onItemSelected = { selectedSchedule ->
                    viewModel.updateTaskField { it.copy(isScheduled = selectedSchedule) }
                })
            Spacer(modifier = Modifier.height(10.dp))

            //if notification permission is denied, display a warning message that the user won't get notifications, and a link to the app settings to change this
            if(isPermissionDenied) {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Notification permission is denied. ",
                        color = Color.Red
                    )
                    Text(
                        text = "Enable in Settings",
                        color = Color.Blue,
                        modifier = Modifier.clickable { openAppSettings(context) }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Row(
                
            ) {
                Button(onClick = {
                    /*if(viewModel.taskTitleState.isNotEmpty() && viewModel.taskDescriptionState.isNotEmpty()) {
                        if(id != 0L) {
                            viewModel.updateTask(
                                Task(
                                    id = id,
                                    title = viewModel.taskTitleState.trim(),
                                    description = viewModel.taskDescriptionState.trim(),
                                    frequency = viewModel.taskFrequencyState.trim(),
                                    implementationWindow =  viewModel.taskImplementationWindowState.trim(),
                                    lastCompletedDate = viewModel.taskLastCompletedState,
                                    nextScheduledDateTime = viewModel.taskNextScheduledState,
                                    isCompleted = viewModel.taskIsCompletedState,
                                    isMissed = viewModel.taskIsMissedState,
                                    isInProgress = viewModel.taskInProgressState,
                                    isScheduled = viewModel.taskIsScheduledState
                                )
                            )
                        } else {
                            viewModel.addTask(
                                Task(
                                    title = viewModel.taskTitleState.trim(),
                                    description = viewModel.taskDescriptionState.trim(),
                                    frequency = viewModel.taskFrequencyState.trim(),
                                    implementationWindow =  viewModel.taskImplementationWindowState.trim(),
                                    lastCompletedDate = viewModel.taskLastCompletedState,
                                    nextScheduledDateTime = viewModel.taskNextScheduledState,
                                    isCompleted = viewModel.taskIsCompletedState,
                                    isMissed = viewModel.taskIsMissedState,
                                    isInProgress = viewModel.taskInProgressState,
                                    isScheduled = viewModel.taskIsScheduledState
                                )
                            )
                            snackMessage.value = "Task has been created"
                        }
                    } else {
                        snackMessage.value = "Enter fields to create a task"
                    }*/
                    if(id != 0L) {
                        viewModel.updateTask(context, task)
                    } else {
                        viewModel.addTask(task)
                        snackMessage.value = "Task has been created"
                    }
                    scope.launch {
                        //scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                        //keyboard takes a minute to disappear, kinda weird looking
                        navController.navigateUp()
                    }
                }) {
                    Text(
                        text = if(id != 0L) stringResource(id = R.string.update_task)
                        else stringResource(id = R.string.add_task), style = TextStyle(fontSize = 18.sp)
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            //scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                            //keyboard takes a minute to disappear, kinda weird looking
                            navController.navigateUp()
                        }
                    }
                ) {
                    Text(text = "Cancel")
                }
            }
            

        }
    }
}