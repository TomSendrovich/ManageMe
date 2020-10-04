package com.e.manageme2.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import com.yalantis.beamazingtoday.interfaces.AnimationType
import com.yalantis.beamazingtoday.interfaces.BatModel
import com.yalantis.beamazingtoday.listeners.BatListener
import com.yalantis.beamazingtoday.listeners.OnItemClickListener
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator
import com.yalantis.beamazingtoday.ui.callback.BatCallback
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView
import kotlinx.android.synthetic.main.activity_main.preview_toolbar
import kotlinx.android.synthetic.main.activity_preview_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.min


class PreviewTaskActivity : AppCompatActivity(), BatListener, OnItemClickListener,
    OnOutsideClickedListener {
    lateinit var mTask: Task

    private var mRecyclerView: BatRecyclerView? = null
    private var mAdapter: BatAdapter? = null
    private var mGoals: ArrayList<BatModel>? = ArrayList()
    private var mAnimator: BatItemAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_task)
        layout_preview_task.layoutDirection = View.LAYOUT_DIRECTION_LTR
        mTask = intent.getSerializableExtra("EXTRA_TASK") as Task

        setSupportActionBar(preview_toolbar)
        title = mTask.taskTitle
        preview_task_body.text = mTask.taskBody


        mRecyclerView = findViewById(R.id.bat_recycler_view)
        mAnimator = BatItemAnimator()
        mRecyclerView?.view?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.view?.adapter =
            BatAdapter(mGoals, this, mAnimator).setOnItemClickListener(this)
                .setOnOutsideClickListener(this).also {
                    mAdapter = it
                }

        mRecyclerView?.view?.layoutManager = LinearLayoutManager(this)
        mRecyclerView?.view?.adapter = mAdapter

        val itemTouchHelper = ItemTouchHelper(BatCallback(this))
        itemTouchHelper.attachToRecyclerView(mRecyclerView?.view)
        mRecyclerView?.view?.itemAnimator = mAnimator
        mRecyclerView?.setAddItemListener(this)

        layout_preview_task.setOnClickListener { mRecyclerView?.revertAnimation() }

        preview_delete_image.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete the task?")
            builder.setPositiveButton("Yes") { dialog, which ->
                deleteTask()
            }
            builder.setNegativeButton("No") { dialog, which ->
                application.motionToastInfo("Task has not been deleted", this)
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
        application.motionToastDelete("Task has been deleted", this)
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun editTask(task: Task) {
        val intent = Intent(applicationContext, AddTaskActivity::class.java)
        intent.putExtra("EXTRA_TASK", task)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun add(string: String) {
        mGoals?.add(0, Goal(string))
        mAdapter?.notify(AnimationType.ADD, 0)
    }

    override fun delete(position: Int) {
        mGoals?.removeAt(position);
        mAdapter?.notify(AnimationType.REMOVE, position)
    }

    override fun move(from: Int, to: Int) {
        if (from >= 0 && to >= 0) {
            mAnimator!!.setPosition(to)
            val model = mGoals!![from]
            mGoals?.remove(model)
            mGoals?.add(to, model)
            mAdapter!!.notify(AnimationType.MOVE, from, to)
            if (from == 0 || to == 0) {
                mRecyclerView!!.view.scrollToPosition(min(from, to))
            }
        }
    }

    override fun onClick(item: BatModel, position: Int) {
        Toast.makeText(this, item.text, Toast.LENGTH_SHORT).show();
    }

    override fun onOutsideClicked() {
        mRecyclerView?.revertAnimation();
    }
}