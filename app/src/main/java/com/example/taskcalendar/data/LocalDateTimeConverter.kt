package com.example.taskcalendar.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateString: String?): LocalDateTime? {
        return dateString?.let {
            LocalDateTime.parse(it, formatter)
        }
    }
}