package com.e.manageme2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import kotlinx.android.synthetic.main.activity_return_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

class ReturnTaskActivity : AppCompatActivity() {
    lateinit var mTask : Task
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return_task)

        mTask = intent.getSerializableExtra("EXTRA_TASK") as Task
        text_view_title_return.setText(mTask.taskTitle)
        text_view_body_return.setText(mTask.taskBody)

        floatingReturnButton.setOnClickListener {
            mTask.currentScore=0.0
            CoroutineScope(EmptyCoroutineContext).launch {
                TaskDatabase(applicationContext).getTaskDao().addTask(mTask)
            }
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
        }

    }
}