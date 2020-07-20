package com.e.manageme2.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

class AddTaskActivity : AppCompatActivity() {

    private var mTask: Task? = null
    lateinit var mTaskTitle: String
    lateinit var mTaskBody: String
    private var mTaskGoal: Double = 0.0
    private var mTaskCurrentScore: Double = 0.0
    var isUpdate: Boolean = false

    private lateinit var spinnerChooseMethod: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var method: String
    private lateinit var chosenDate: String
    private lateinit var chosenDay: String
    private var methodId: Int = 0

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val task = intent.getSerializableExtra("EXTRA_TASK") as? Task



        val methodChoose = arrayOf("Daily", "Weekly", "Monthly")
        val daysOfWeek =
            arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val daysOfMonth = arrayOf(
            "1st",
            "2nd",
            "3rd",
            "4th",
            "5th",
            "6th",
            "7th",
            "8th",
            "9th",
            "10th",
            "11th",
            "12th",
            "13th",
            "14th",
            "15th",
            "16th",
            "17th",
            "18th",
            "19th",
            "20th",
            "21th",
            "22th",
            "22th",
            "23th",
            "24th",
            "25th",
            "26th",
            "27th",
            "28th"
        )

        spinnerChooseMethod = findViewById(R.id.spinner_choose_method)
        spinnerChooseMethod.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, methodChoose)

        spinnerChooseMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                method = methodChoose[position]
                if (method.compareTo("Daily") == 0) {
                    start_day.visibility = View.INVISIBLE
                    spinner_weekly.visibility = View.INVISIBLE

                    start_date_month.visibility = View.INVISIBLE
                    spinner_month.visibility = View.INVISIBLE

                    methodId=0
                }
                if (method.compareTo("Weekly") == 0) {
                    start_day.visibility = View.VISIBLE
                    spinner_weekly.visibility = View.VISIBLE

                    start_date_month.visibility = View.INVISIBLE
                    spinner_month.visibility = View.INVISIBLE

                    methodId=1
                }
                if (method.compareTo("Monthly") == 0) {
                    start_day.visibility = View.INVISIBLE
                    spinner_weekly.visibility = View.INVISIBLE

                    start_date_month.visibility = View.VISIBLE
                    spinner_month.visibility = View.VISIBLE

                    methodId=2
                }
            }
        }

        spinnerMonth = findViewById(R.id.spinner_month)
        spinnerMonth.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysOfMonth)
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenDate = daysOfMonth[position]
            }

        }

        spinnerDay = findViewById(R.id.spinner_weekly)
        spinnerDay.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysOfWeek)
        spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenDay = daysOfWeek[position]
            }
        }


        if (task != null) {
            edit_text_title.setText(task.taskTitle)
            edit_text_desc.setText(task.taskBody)
            edit_text_goal.setText(task.goal.toString())
            edit_text_current_score.setText(task.currentScore.toString())
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
                edit_text_title.error = "Title required"
                edit_text_title.requestFocus()
                return@setOnClickListener
            }
            if (mTaskBody.isEmpty()) {
                edit_text_desc.error = "Title required"
                edit_text_desc.requestFocus()
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
                application.toast("Task updated successfully")
            } else {
                application.toast("Task saved successfully")
            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }

    }
}