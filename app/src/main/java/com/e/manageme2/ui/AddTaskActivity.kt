package com.e.manageme2.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beardedhen.androidbootstrap.TypefaceProvider
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand
import kotlin.collections.ArrayList

class AddTaskActivity : AppCompatActivity() {
    private var mTask: Task? = null
    lateinit var mTaskTitle: String
    lateinit var mTaskBody: String
    private var mTaskGoal: Double = 0.0
    private var mTaskCurrentScore: Double = 0.0
    var isUpdate: Boolean = false
    private var methodId: Int = 0

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        TypefaceProvider.registerDefaultIconSets()

        val task = intent.getSerializableExtra("EXTRA_TASK") as? Task



        if (task != null) {
            edit_text_title.setText(task.taskTitle)
            edit_text_desc.setText(task.taskBody)
            edit_text_goal.setText(task.goal.toString())
            edit_text_current_score.setText(task.currentScore.toString())
            if (task.methodId == 0) {
                daily_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                methodId = 0
            } else if (task.methodId == 1) {
                weekly_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                methodId = 1
            } else {
                monthly_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
                methodId = 2
            }
        }


        daily_button.setOnClickListener {
            methodId = 0
            daily_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
            weekly_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            monthly_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
        }
        weekly_button.setOnClickListener {
            methodId = 1
            daily_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            weekly_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
            monthly_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
        }
        monthly_button.setOnClickListener {
            methodId = 2
            daily_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            weekly_button.bootstrapBrand = DefaultBootstrapBrand.REGULAR
            monthly_button.bootstrapBrand = DefaultBootstrapBrand.SUCCESS
        }

        button_save.setOnClickListener {
            mTaskTitle = edit_text_title.text.toString().trim()
            mTaskBody = edit_text_desc.text.toString().trim()


            //Checks if the Goal is filled with a number
            try {
                mTaskGoal = edit_text_goal.text.toString().trim().toDouble()
            } catch (ex: NumberFormatException) {
                edit_text_goal.error = "Goal required"
                return@setOnClickListener
            }

            //Checks if the Current Score is filled with a number
            try {
                mTaskCurrentScore = edit_text_current_score.text.toString().trim().toDouble()
            } catch (ex: NumberFormatException) {
                edit_text_current_score.error = "Current score required"
                return@setOnClickListener
            }

            if (mTaskTitle.isEmpty()) {
                application.motionToastError("Title is required", this)
                //edit_text_title.error = "Title required"
                //edit_text_title.requestFocus()
                return@setOnClickListener
            }
            if (mTaskBody.isEmpty()) {
                application.motionToastError("Description is required", this)
                //edit_text_desc.error = "Title required"
                //edit_text_desc.requestFocus()
                return@setOnClickListener
            }

            //creating a coroutine scope
            CoroutineScope(EmptyCoroutineContext).launch {
                mTask = Task(
                    mTaskTitle,
                    mTaskBody,
                    mTaskGoal,
                    mTaskCurrentScore,
                    false,
                    methodId
                )
                //Checks if we have an extra for the intent, if it does its an updated task.
                //else, it is a new task
                if (task == null) {
                    //adding the task to the database
                    TaskDatabase(applicationContext).getTaskDao().addTask(mTask!!)
                    isUpdate = false
                } else {
                    //updating a task
                    mTask!!.taskId = task.taskId
                    TaskDatabase(applicationContext).getTaskDao().updateTask(mTask!!)
                    isUpdate = true
                }
            }
            if (isUpdate) {
                application.motionToastSuccess("Task updated successfully", this)
                //application.toast("Task updated successfully")
            } else {
                application.motionToastSuccess("Task saved successfully", this)
                //application.toast("Task saved successfully")
            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }
}