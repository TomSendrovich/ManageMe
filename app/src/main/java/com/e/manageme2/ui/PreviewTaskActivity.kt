package com.e.manageme2.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.preview_toolbar
import kotlinx.android.synthetic.main.activity_preview_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.EmptyCoroutineContext

class PreviewTaskActivity : AppCompatActivity() {
    lateinit var mTask: Task
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_task)
        layout_preview_task.layoutDirection = View.LAYOUT_DIRECTION_LTR
        mTask = intent.getSerializableExtra("EXTRA_TASK") as Task

        setSupportActionBar(preview_toolbar)
        title = mTask.taskTitle
        preview_task_body.text = mTask.taskBody

        //val timeDiff: Long = mTask.goalTime.timeInMillis - Calendar.getInstance().timeInMillis
        //preview_task_days_count.text = "Days left: " + (TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS)+1)

        preview_delete_image.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete the task?")
            builder.setPositiveButton("Yes"){dialog, which ->
                deleteTask()
            }
            builder.setNegativeButton("No"){dialog, which ->
                application.motionToastInfo("Task has not been deleted",this)
            }
            builder.show()
        }

        preview_edit_image.setOnClickListener {
            editTask(mTask)
        }
    }

    private fun deleteTask() {
        CoroutineScope(EmptyCoroutineContext).launch {
            TaskDatabase(applicationContext).getTaskDao().deleteTask(mTask)
        }
        application.motionToastDelete("Task has been deleted",this)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun editTask(task: Task) {
        val intent = Intent(applicationContext, AddTaskActivity::class.java)
        intent.putExtra("EXTRA_TASK",task)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}