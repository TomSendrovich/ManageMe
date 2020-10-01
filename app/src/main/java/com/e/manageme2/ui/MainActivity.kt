package com.e.manageme2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private var mTask: Task? = null
    private var mAllTasks: MutableList<Task> = ArrayList()
    private var mFinishedTasks: MutableList<Task> = ArrayList()
    private val notFinishedTasks: MutableList<Task> = ArrayList()
    private lateinit var adapter : TasksAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar :Toolbar
    var currTime = Calendar.getInstance()

    private var menuObjects :MutableList<MenuObject> = ArrayList()
    private lateinit var mContextMenuDialogFragmet : ContextMenuDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Context Menu
        initToolbar()
        initMenu()


      //  layout_dashboard.layoutDirection= View.LAYOUT_DIRECTION_LTR

        mTask = intent.getSerializableExtra("EXTRA_TASK") as? Task

        //Checks if there is a returned task
        if(mTask!=null){
            mTask!!.currentScore= 0.0
            CoroutineScope(EmptyCoroutineContext).launch {
                TaskDatabase(applicationContext).getTaskDao().addTask(mTask!!)
            }
        }

        //the recycler now has a fixed size
        recyclerView = findViewById(R.id.recycler_view_tasks)
        recyclerView.setHasFixedSize(true)
        //recyclerView.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,3)
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
        adapter = TasksAdapter(applicationContext,notFinishedTasks)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        button_add.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initToolbar(){
        toolbar=findViewById(R.id.preview_toolbar)
        setSupportActionBar(toolbar)
        title = "All Tasks"
        toolbar.title="All Tasks"
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.apply {
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
            isClosableOutside = false,
            gravity = MenuGravity.START

            // set other settings to meet your needs
        )
        mContextMenuDialogFragmet = ContextMenuDialogFragment.newInstance(menuParams).apply {
            menuItemClickListener = {view, position ->
                if(position==1){
                    CoroutineScope(EmptyCoroutineContext).launch {
                        mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllDailyTasks()
                    }
                    sleep(500)
                    adapter = TasksAdapter(applicationContext,mAllTasks)
                    recyclerView.adapter = adapter

                    toolbar.title= "Daily Tasks"
                    adapter.notifyDataSetChanged()
                }
                if(position==2){
                    CoroutineScope(EmptyCoroutineContext).launch {
                        mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllWeeklyTasks()
                    }
                    sleep(500)
                    adapter = TasksAdapter(applicationContext,mAllTasks)
                    recyclerView.adapter = adapter
                    val daysLeftToFinsih = abs(currTime.get(Calendar.DAY_OF_WEEK)-7)
                    toolbar.title="Weekly Tasks - Days Left: $daysLeftToFinsih"
                    adapter.notifyDataSetChanged()
                }
                if(position==3){
                    CoroutineScope(EmptyCoroutineContext).launch {
                        mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllMonthlyTasks()
                    }
                    sleep(500)
                    adapter = TasksAdapter(applicationContext,mAllTasks)
                    val daysLeftToFinsih = abs(currTime.get(Calendar.DAY_OF_MONTH)-currTime.getActualMaximum(Calendar.DAY_OF_MONTH))
                    toolbar.title="Monthly Tasks - Days Left: $daysLeftToFinsih"
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                if(position==4){
                    CoroutineScope(EmptyCoroutineContext).launch {
                        mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllTasks()
                    }
                    sleep(500)
                    adapter = TasksAdapter(applicationContext,mAllTasks)
                    toolbar.title="All Tasks"
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                if(position==5){
                    CoroutineScope(EmptyCoroutineContext).launch {
                        mAllTasks = TaskDatabase(applicationContext).getTaskDao().getAllTasks()
                    }
                    sleep(500)
                    adapter = TasksAdapter(applicationContext,mFinishedTasks)
                    toolbar.title="Finished"
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
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
        daily.drawable = getDrawable(R.drawable.ic_24hours)
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
        val allTasks = MenuObject("All Tasks")
        allTasks.drawable = getDrawable(R.drawable.ic_dedent_all)
        allTasks.dividerColor = R.color.bkgndColour
        allTasks.setBgResourceValue(R.color.menu_item_background)
        val allFinishedTasks = MenuObject("Finished")
        allFinishedTasks.drawable = getDrawable(R.drawable.ic_done)
        allFinishedTasks.dividerColor = R.color.bkgndColour
        allFinishedTasks.setBgResourceValue(R.color.menu_item_background)

        menuObjects.add(close)
        menuObjects.add(daily)
        menuObjects.add(weekly)
        menuObjects.add(monthly)
        menuObjects.add(allTasks)
        menuObjects.add(allFinishedTasks)

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