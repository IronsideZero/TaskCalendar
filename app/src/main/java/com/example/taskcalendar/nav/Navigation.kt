package com.example.taskcalendar.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.taskcalendar.TaskController
import com.example.taskcalendar.viewmodels.AddEditTaskViewModel
import com.example.taskcalendar.viewmodels.CalendarMonthViewModel
import com.example.taskcalendar.viewmodels.CalendarWeekViewModel
import com.example.taskcalendar.viewmodels.MainViewModel
import com.example.taskcalendar.viewmodels.TaskListViewModel
import com.example.taskcalendar.views.*

@Composable
fun Navigation(navController: NavHostController = rememberNavController(), taskController: TaskController = viewModel()) {
    NavHost(
        navController = navController,
        startDestination = RouteList.MainView.route
    ) {
        composable(RouteList.MainView.route) {
            //using the viewmodel factory allows the instantiation of a custom viewmodel with passed parameters
            val viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
            MainView(taskController, viewModel, navController)
        }
        composable(RouteList.CalendarMonthView.route) {
            val viewModel: CalendarMonthViewModel = viewModel(factory = CalendarMonthViewModel.Factory)
            CalendarMonthView(taskController, viewModel, navController)
        }
        composable(RouteList.CalendarWeekView.route) {
            val viewModel: CalendarWeekViewModel = viewModel(factory = CalendarWeekViewModel.Factory)
            CalendarWeekView(taskController, viewModel, navController)
        }
        //parameter becomes argument, which is then passed to AddEditTaskView, which uses the value to determine what to display
        composable(RouteList.AddEditTaskView.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                }
            )
        ) {entry ->
            val id = if(entry.arguments != null) entry.arguments!!.getLong("id") else 0L
            val viewModel: AddEditTaskViewModel = viewModel(factory = AddEditTaskViewModel.Factory)
            AddEditTaskView(id = id, taskController, viewModel, navController = navController)
        }
        composable(RouteList.TaskListView.route) {
            val viewModel: TaskListViewModel = viewModel(factory = TaskListViewModel.Factory)
            TaskListView(taskController, viewModel, navController)
        }
    }
}