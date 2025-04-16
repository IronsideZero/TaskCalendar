package com.example.taskcalendar.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskcalendar.R
import com.example.taskcalendar.data.Task
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar


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
fun MonthHeader(daysOfWeek: List<DayOfWeek>, currentMonth: CalendarMonth) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            text = currentMonth.yearMonth.month.name.toString() + " " + currentMonth.yearMonth.year.toString(),
            fontWeight = FontWeight.Medium
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                text = dayOfWeek.name.substring(0,3),
                fontWeight = FontWeight.Medium
            )
        }
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

@Composable
fun TaskDatePickerField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
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
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                contentDescription = "Select Date",
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(8.dp),
                tint = Color.Black
            )
        }
    )
}

@Composable
fun TaskDatePickerButton(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp), // Matches TextField corner radius
        border = BorderStroke(1.dp, colorResource(id = R.color.black)), // Matches TextField border
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent, // No background, like a TextField
            contentColor = Color.Black // Text color
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp) // Mimic label spacing
                )
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                contentDescription = "Select Date",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun TaskTimePickerButton(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp), // Matches TextField corner radius
        border = BorderStroke(1.dp, colorResource(id = R.color.black)), // Matches TextField border
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent, // No background, like a TextField
            contentColor = Color.Black // Text color
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp) // Mimic label spacing
                )
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_access_time_24),
                contentDescription = "Select Date",
                tint = Color.Black
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerPopup(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                var dateAsMillis = datePickerState.selectedDateMillis
                var dateAsLocalDate = dateAsMillis?.let { millis ->
                    Instant.ofEpochMilli(millis)
                        .atOffset(ZoneOffset.UTC)
                        .toLocalDate()
                }
                onDateSelected(dateAsLocalDate)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerPopup(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            elevation = 8.dp
        ) {
            Column {
                TimePicker(
                    state = timePickerState,
                )
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
                Button(onClick = {onConfirm(timePickerState)}) {
                    Text("Confirm")
                }
            }
        }

    }

}

@Composable
fun DropDownSelector(
    label: String,
    value: String,
    options: List<String>,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp), // Matches TextField corner radius
        border = BorderStroke(1.dp, colorResource(id = R.color.black)), // Matches TextField border
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent, // No background, like a TextField
            contentColor = Color.Black // Text color
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp) // Mimic label spacing
                )
                Text(
                    text = value,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = "Select Date",
                tint = Color.Black
            )
        }
        var expanded by remember { mutableStateOf(false)}
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for(item: String in options) {
                DropdownMenuItem(
                    text = { item },
                    onClick = { /*TODO figure out how to pass this back to the view, and then the viewmodel*/ }
                )
            }
        }
    }
}

@Composable
fun DropDownSelector2(
    label: String,
    selectedValue: String,
    options: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true }, // Open dropdown on click
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, colorResource(id = R.color.black)),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent, // No background, like a TextField
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = selectedValue,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = "Select an option",
                tint = Color.Black
            )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        options.forEach { item ->
            DropdownMenuItem(
                text = { Text(item) },
                onClick = {
                    onItemSelected(item) // Pass selected item to parent
                    expanded = false // Close dropdown
                }
            )
        }
    }
}

@Composable
fun CheckboxSelector(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = label)
    }
}

