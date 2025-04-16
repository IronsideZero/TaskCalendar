package com.example.taskcalendar.utils

import com.example.taskcalendar.data.Task
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utilities {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateOnlyFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    fun ToJson(task: Task): String {
        val nextScheduledDateTimeString = task.nextScheduledDateTime?.format(formatter) ?: "null"
        val lastCompletedDateTimeString = task.lastCompletedDate?.format(dateOnlyFormatter) ?: "null"


        return """
            {
              "id": ${task.id},
              "title": "${escape(task.title)}",
              "description": "${escape(task.description)}",
              "frequency": "${escape(task.frequency)}",
              "implementationWindow": "${escape(task.implementationWindow)}",
              "nextScheduledDateTime": ${if (task.nextScheduledDateTime == null) "null" else "\"$nextScheduledDateTimeString\""},
              "lastCompletedDate": ${if (task.nextScheduledDateTime == null) "null" else "\"$lastCompletedDateTimeString\""},
              "isCompleted": ${task.isCompleted},
              "isMissed": ${task.isMissed},
              "isInProgress": ${task.isInProgress},
              "isScheduled": ${task.isScheduled}
            }
        """.trimIndent()
    }

    fun FromJson(json: String): Task {
        // Remove whitespace and outer braces
        val cleaned = json.trim().removeSurrounding("{", "}").trim()
        val fields = cleaned.split(",\n").associate {
            val (key, value) = it.trim().split(":", limit = 2)
            key.trim().removeSurrounding("\"") to value.trim()
        }

        val id = fields["id"]?.toLong() ?: throw IllegalArgumentException("Missing id")
        val title = fields["title"]?.removeSurrounding("\"")?.unescape() ?: ""
        val description = fields["description"]?.removeSurrounding("\"")?.unescape() ?: ""
        val frequency = fields["frequency"]?.removeSurrounding("\"")?.unescape() ?: ""
        val implementationWindow = fields["implementationWindow"]?.removeSurrounding("\"")?.unescape() ?: ""
        val nextScheduledDateTime = fields["nextScheduledDateTime"]?.let {
            LocalDateTime.parse(it.removeSurrounding("\""), formatter)
        } ?: LocalDateTime.now()
        val lastCompletedDate = fields["lastCompletedDate"]?.let {
            LocalDate.parse(it.removeSurrounding("\""), dateOnlyFormatter)
        } ?: LocalDate.now()
        val isCompleted = fields["isCompleted"]?.toBoolean() ?: false
        val isMissed = fields["isMissed"]?.toBoolean() ?: false
        val isInProgress = fields["isInProgress"]?.toBoolean() ?: false
        val isScheduled = fields["isScheduled"]?.removeSurrounding("\"")?.unescape() ?: ""



        return Task(id, title, description, frequency, implementationWindow, lastCompletedDate, nextScheduledDateTime, isCompleted, isMissed, isInProgress, isScheduled)
    }

    private fun escape(text: String): String {
        return text.replace("\"", "\\\"") // escape quotes
            .replace("\n", "\\n")   // escape newlines
            .replace("\r", "\\r")
    }
    private fun String.unescape(): String {
        return this.replace("\\\"", "\"").replace("\\n", "\n").replace("\\r", "\r")
    }
}

