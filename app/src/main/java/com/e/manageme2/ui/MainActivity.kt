package com.e.manageme2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.e.manageme2.R
import com.e.manageme2.db.Task
import com.e.manageme2.db.TaskDatabase
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment
import com.yalantis.contextmenu.lib.MenuGravity
import com.yalantis.contextmenu.lib.MenuObject
import com.yalantis.contextmenu.lib.MenuParams
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    private var mTask: Task? = null
    //private var mHeader: String = "Finished"
    private var mAllTasks: MutableList<Task> = ArrayList()
    private var mFinishedTasks: MutableList<Task> = ArrayList()
    private val notFinishedTasks: MutableList<Task> = ArrayList()

    private var menuObjects :MutableList<MenuObject> = ArrayList()
    private lateinit var mContextMenuDialogFragmet : ContextMenuDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Context Menu
        initToolbar()
        initMenu()

        layout_dashboard.layoutDirection= View.LAYOUT_DIRECTION_LTR

        mTask = intent.getSerializableExtra("EXTRA_TASK") as? Task

        //Checks if there is a returned task
        if(mTask!=null){
            mTask!!.currentScore= 0.0
            CoroutineScope(EmptyCoroutineContext).launch {
                TaskDatabase(applicationContext).getTaskDao().addTask(mTask!!)
            }
        }

        //the recycler now has a fixed size
        recycler_view_tasks.setHasFixedSize(true)
        recycler_view_tasks.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        CoroutineScope(EmptyCoroutineContext).launch {
            mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllTasks()
        }
        sleep(1000)
        //adding all the finished tasks to the finishedTasks list
        for (task in mAllTasks) {
            if (task.goal == task.currentScore) {
                mFinishedTasks.add(task)
            } else {
                notFinishedTasks.add(task)
            }
        }

        //Adding all the non-finished tasks to the recycler view
        val adapter = TasksAdapter(applicationContext,notFinishedTasks)
        recycler_view_tasks.adapter = adapter
        adapter.notifyDataSetChanged()
        //(recycler_view_tasks.adapter as TasksAdapter).notifyDataSetChanged()

        //Adding the expandable list adapter
        /*expandable_list_view.setAdapter(
            ExpandableListAdapter(
                applicationContext,
                mHeader,
                mFinishedTasks
            )
        )
        expandable_list_view.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            //todo:Intent to ReturnTaskActivity with the Task that return
            val intent = Intent(applicationContext, AddTaskActivity::class.java)
            //intent.putExtra("EXTRA_TASK",parent.getChildAt(id.toInt()))
            startActivity(intent)
            return@setOnChildClickListener true
        }*/

        button_add.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initToolbar(){
        setSupportActionBar(preview_toolbar)
        title = "All Tasks"
        preview_toolbar.title="All Tasks"
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            //setDisplayShowTitleEnabled(false)
        }
        preview_toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener{onBackPressed()}
        }
    }

    override fun onBackPressed() {
        if (::mContextMenuDialogFragmet.isInitialized && mContextMenuDialogFragmet.isAdded) {
            mContextMenuDialogFragmet.dismiss()
        } else {
            finish()
        }
    }

    private fun initMenu(){
        val menuParams = MenuParams(
            actionBarSize = resources.getDimension(R.dimen.tool_bar_height).toInt(),
            menuObjects = getMenuObjects(),
            isClosableOutside = false
            // set other settings to meet your needs
        )
        mContextMenuDialogFragmet = ContextMenuDialogFragment.newInstance(menuParams).apply {
            menuItemClickListener = {view, position ->
                if(position==1){

                }
                if(position==2){

                }
                if(position==3){

                }
            }
        }
    }

    private fun getMenuObjects() : MutableList<MenuObject>{

        val close = MenuObject("Close")
        close.dividerColor = R.color.bkgndColour
        close.drawable = getDrawable(R.drawable.ic_close)
        close.setBgResourceValue(R.color.menu_item_background)
        val daily = MenuObject("Daily Tasks")
        daily.drawable = getDrawable(R.drawable.ic_daily)
        daily.dividerColor = R.color.bkgndColour
        daily.setBgResourceValue(R.color.menu_item_background)
        val monthly = MenuObject("Monthly Tasks")
        monthly.drawable = getDrawable(R.drawable.ic_bettermonth)
        monthly.dividerColor = R.color.bkgndColour
        monthly.setBgResourceValue(R.color.menu_item_background)
        val weekly = MenuObject("Weekly Tasks")
        weekly.drawable = getDrawable(R.drawable.ic_week)
        weekly.dividerColor = R.color.bkgndColour
        weekly.setBgResourceValue(R.color.menu_item_background)

        menuObjects.add(close)
        menuObjects.add(daily)
        menuObjects.add(weekly)
        menuObjects.add(monthly)

        return menuObjects
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.context_menu -> {
                    showContextMenuDialogFragment()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showContextMenuDialogFragment(){
        if (supportFragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
            mContextMenuDialogFragmet.show(supportFragmentManager, ContextMenuDialogFragment.TAG)
        }
    }
}