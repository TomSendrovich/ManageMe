package com.e.manageme2.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.e.manageme2.R
import com.e.manageme2.db.Task

class ExpandableListAdapter(
    private var context: Context?,
    private var header: String,
    private var finishedTasks: MutableList<Task>
) : BaseExpandableListAdapter() {
    override fun getGroup(groupPosition: Int): String {
        return header
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_group,null)

        }
        val title = convertView?.findViewById<TextView>(R.id.finished_task_title)
        title?.text = header
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return finishedTasks.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): String {
        return finishedTasks[childPosition].taskTitle
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View? {
        var convertView = convertView
        if(convertView == null){
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.layout_child,null)

        }
        val title = convertView?.findViewById<TextView>(R.id.finished_task_title)
        title?.text = getChild(groupPosition,childPosition)
        title?.setOnClickListener {
            //todo:Intent to ReturnTaskActivity with the Task that return
            val intent = Intent(context, ReturnTaskActivity::class.java)
            intent.putExtra("EXTRA_TASK",finishedTasks[childPosition])
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }
        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return 1
    }

}