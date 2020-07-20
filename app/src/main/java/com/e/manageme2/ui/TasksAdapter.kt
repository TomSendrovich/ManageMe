package com.e.manageme2.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.e.manageme2.R
import com.e.manageme2.db.Task
import kotlinx.android.synthetic.main.layout_task.view.*
import java.util.*
import java.util.concurrent.TimeUnit


class  TasksAdapter(private val context: Context, private val tasks : List<Task>) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_task,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.view.task_title.text = tasks[position].taskTitle
        holder.view.task_body.text = tasks[position].taskBody
        holder.view.task_count.text =tasks[position].currentScore.toString()+"/"+tasks[position].goal.toString()
        //val timeDiff: Long = tasks[position].goalTime.timeInMillis - Calendar.getInstance().timeInMillis
        //holder.view.task_days_count.text = "Days Left: " + (TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS)+1)

        //update a task
        holder.view.setOnClickListener {
            val intent = Intent(context, PreviewTaskActivity::class.java)
            intent.putExtra("EXTRA_TASK",tasks[position])
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }


    class TaskViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}