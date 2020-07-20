package com.e.manageme2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.e.manageme2.R
import com.e.manageme2.db.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.preview_toolbar
import kotlinx.android.synthetic.main.activity_preview_task.*
import java.util.*
import java.util.concurrent.TimeUnit

class PreviewTaskActivity : AppCompatActivity() {
    lateinit var mTask : Task
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_task)
        layout_preview_task.layoutDirection = View.LAYOUT_DIRECTION_LTR
        mTask=intent.getSerializableExtra("EXTRA_TASK") as Task

        setSupportActionBar(preview_toolbar)
        title = mTask.taskTitle
        preview_task_body.text = mTask.taskBody

        //val timeDiff: Long = mTask.goalTime.timeInMillis - Calendar.getInstance().timeInMillis
        //preview_task_days_count.text = "Days left: " + (TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS)+1)


    }
}