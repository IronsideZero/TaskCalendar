package com.example.taskcalendar.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeConverter::class,
    LocalDateConverter::class
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
}