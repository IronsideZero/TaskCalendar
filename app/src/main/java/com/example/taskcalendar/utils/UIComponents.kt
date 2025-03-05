package com.example.taskcalendar.utils

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.taskcalendar.R
import com.example.taskcalendar.data.Task
import com.kizitonwose.calendar.core.CalendarDay


@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            .clickable { onClick() },
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)){
            Text(text = task.title, fontWeight = FontWeight.ExtraBold)
            Text(text = task.description)
        }
    }
}

@Composable
fun TaskTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(value = value, onValueChange = onValueChange,
        label = { Text(text = label, color = Color.Black)},
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            //can use predefined colors, or own colors in Res.values.colors.xml
            textColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black),
        )
    )
}