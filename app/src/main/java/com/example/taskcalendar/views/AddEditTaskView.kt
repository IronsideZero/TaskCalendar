package com.example.taskcalendar.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.taskcalendar.TaskController
import com.example.taskcalendar.data.Task
import com.example.taskcalendar.utils.TaskTextField
import com.example.taskcalendar.viewmodels.AddEditTaskViewModel

@Composable
fun AddEditTaskView(
    id: Long,
    controller: TaskController,
    viewModel: AddEditTaskViewModel,
    navController: NavController
) {
    val snackMessage = remember{
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    //scaffoldState is used to update the contents of the scaffold immediately upon change
    val scaffoldState = rememberScaffoldState()

    if(id != 0L) {
        val task = controller.getTaskByID(id).collectAsState(initial = Task(0L, "", ""))
        //changing these values directly? Feels like we should be using viewModel.onWishTitleChanged(wish.value.title) like we do below, since that's the whole point of having these methods

        viewModel.taskTitleState = task.value.title
        viewModel.taskDescriptionState = task.value.description
        viewModel.taskFrequencyState = task.value.frequency
        viewModel.taskImplementationWindowState = task.value.implementationWindow
        viewModel.taskLastCompletedState = task.value.lastCompletedDate
        viewModel.taskNextScheduledState = task.value.nextScheduledDateTime
        viewModel.taskIsCompletedState = task.value.isCompleted
        viewModel.taskIsMissedState = task.value.isMissed
        viewModel.taskInProgressState = task.value.isInProgress
        viewModel.taskIsScheduledState = task.value.isScheduled
    } else {
        viewModel.taskTitleState = ""
        viewModel.taskDescriptionState = ""
        viewModel.taskFrequencyState = ""
        viewModel.taskImplementationWindowState = 3
        viewModel.taskLastCompletedState = null
        viewModel.taskNextScheduledState = null
        viewModel.taskIsCompletedState = false
        viewModel.taskIsMissedState = false
        viewModel.taskInProgressState = false
        viewModel.taskIsScheduledState = false
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            TaskTextField(label = "Title", value = viewModel.taskTitleState, onValueChange = {viewModel.onTaskTitleChange(it)})
            Spacer(modifier = Modifier.height(10.dp))
            TaskTextField(label = "Description", value = viewModel.taskDescriptionState, onValueChange = {viewModel.onTaskDescriptionChange(it)})
        }
    }
}