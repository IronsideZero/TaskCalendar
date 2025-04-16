package com.example.taskcalendar.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime


@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name="title")
    var title: String = "",
    @ColumnInfo(name="description")
    var description: String = "",
    @ColumnInfo(name="frequency")
    var frequency: String = "Weekly",
    @ColumnInfo(name="implementation_window")
    var implementationWindow: String = "3",
    @ColumnInfo(name="last_completed_date")
    var lastCompletedDate: LocalDate = LocalDate.now(),
    @ColumnInfo(name="next_scheduled_date")
    var nextScheduledDateTime: LocalDateTime = LocalDateTime.now().plusDays(1),
    @ColumnInfo(name="is_completed")
    var isCompleted: Boolean = false,
    @ColumnInfo(name="is_missed")
    var isMissed: Boolean = false,
    @ColumnInfo(name="is_in_progress")
    var isInProgress: Boolean = false,
    @ColumnInfo(name="is_scheduled")
    var isScheduled: String = "Unscheduled"
    )