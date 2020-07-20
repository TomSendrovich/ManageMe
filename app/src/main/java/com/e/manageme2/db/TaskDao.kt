package com.e.manageme2.db

import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask (task: Task)

    @Query("SELECT * FROM Task ORDER BY taskId DESC")
    fun getAllTasks() : MutableList<Task>

    @Query("SELECT * FROM Task WHERE methodId=0 ORDER BY taskId DESC")
    fun getAllDailyTasks():MutableList<Task>

    @Query("SELECT * FROM Task WHERE methodId=1 ORDER BY taskId DESC")
    fun getAllWeeklyTasks():MutableList<Task>

    @Query("SELECT * FROM Task WHERE methodId=2 ORDER BY taskId DESC")
    fun getAllMonthlyTasks():MutableList<Task>

    @Insert
    suspend fun addMultipleTasks (vararg task: Task)

    @Update
    suspend fun updateTask(task:Task)

    @Delete
    suspend fun deleteTask(task:Task)
}