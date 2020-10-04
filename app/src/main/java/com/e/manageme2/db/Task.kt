package com.e.manageme2.db

import androidx.room.*
import com.e.manageme2.ui.Goal
import java.io.Serializable


@Entity
data class Task(
    val taskTitle: String,
    val taskBody: String,
    val goal: Double,
    var currentScore: Double,
    var isCompleted: Boolean = false,
    var methodId: Int
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var taskId: Int = 0
}

