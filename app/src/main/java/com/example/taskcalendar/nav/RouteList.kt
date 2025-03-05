package com.example.taskcalendar.nav

sealed class RouteList(val route: String) {
    object MainView: RouteList("main_view")
    object CalendarMonthView: RouteList("calendar_month_view")
    object CalendarWeekView: RouteList("calendar_week_view")
    object AddEditTaskView: RouteList("add_edit_task_view")
    object TaskListView: RouteList("task_list_view")
}