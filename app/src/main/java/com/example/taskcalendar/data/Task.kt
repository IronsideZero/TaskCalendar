package com.example.taskcalendar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date


@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="title")
    val title: String = "",
    @ColumnInfo(name="description")
    val description: String = "",
    @ColumnInfo(name="frequency")
    val frequency: String = "Weekly",
    @ColumnInfo(name="implementation_window")
    val implementationWindow: Int = 3,
    @ColumnInfo(name="last_completed_date")
    val lastCompletedDate: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name="next_scheduled_date")
    val nextScheduledDateTime: LocalDateTime = LocalDateTime.now().plusDays(1),
    @ColumnInfo(name="is_completed")
    val isCompleted: Boolean = false,
    @ColumnInfo(name="is_missed")
    val isMissed: Boolean = false,
    @ColumnInfo(name="is_in_progress")
    val isInProgress: Boolean = false,
    @ColumnInfo(name="is_scheduled")
    val isScheduled: Boolean = false
    )