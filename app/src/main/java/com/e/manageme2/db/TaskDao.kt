package com.e.manageme2.db

import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask (task: Task)

    @Query("SELECT * FROM Task ORDER BY taskId DESC")
    fun getAllTasks() : MutableList<Task>

    @Insert
    suspend fun addMultipleTasks (vararg task: Task)

    @Update
    suspend fun updateTask(task:Task)

    @Delete
    suspend fun deleteTask(task:Task)
}