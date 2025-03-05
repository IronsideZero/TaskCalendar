package com.example.taskcalendar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TaskDAO {
    //need to add suspend to Insert, Update and Delete functions to force them to run asynchronously, but Query functions don't need it because they use Flow, which is already asynchronous
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addTask(taskEntity: Task)

    @Query("SELECT * FROM `task_table`")
    abstract fun getAllTasks(): Flow<List<Task>>

    @Update
    abstract suspend fun updateTask(taskEntity: Task)

    @Delete
    abstract suspend fun deleteTask(taskEntity: Task)

    @Query("SELECT * FROM `task_table` WHERE id=:id")
    abstract fun getTaskById(id:Long): Flow<Task>

    //TODO get tasks by date range
    //TODO get tasks by completion status
    //TODO get tasks by scheduled status
}