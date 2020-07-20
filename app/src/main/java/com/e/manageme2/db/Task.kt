package com.e.manageme2.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

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

