package com.example.taskcalendar.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.taskcalendar.TaskController
import com.example.taskcalendar.nav.RouteList
import com.example.taskcalendar.utils.Day
import com.example.taskcalendar.utils.TaskItem
import com.example.taskcalendar.viewmodels.MainViewModel
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainView(
    controller: TaskController,
    viewModel: MainViewModel,
    navController: NavController
) {

    //variables for the bottom sheet
    val isSheetFullScreen by remember{ mutableStateOf(false) }
    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    //variables for calendar
    val currentMonth = remember { YearMonth.now()}
    val startMonth = remember {currentMonth.minusMonths(100)}
    val endMonth = remember {currentMonth.plusMonths(100)}
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale()}
    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )


    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {

        }
        ) {
        Scaffold(
            topBar = {

            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(all = 20.dp),
                    contentColor = Color.White,
                    backgroundColor = Color.Black,
                    onClick = { navController.navigate(RouteList.AddEditTaskView.route + "/0L") }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalCalendar(
                state = calendarState,
                dayContent = { Day(it)}
            )
            Divider(color = Color.LightGray)
            Spacer(modifier = Modifier.height(10.dp))
            val taskList = controller.getAllTasks.collectAsState(initial = listOf())
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                //list of tasks goes here
                items(taskList.value, key={task -> task.id}) {
                    task ->
                        //for each Task, make a TaskItem composable, and for the onclick function, pass the id and navigate to the add/edit page with that id
                        TaskItem(task) {
                            val id = task.id
                            navController.navigate(RouteList.AddEditTaskView.route + "/$id")
                        }
                }
            }
        }
    }
}