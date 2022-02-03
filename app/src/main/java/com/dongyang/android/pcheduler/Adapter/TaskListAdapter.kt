package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.ItemListBinding
import com.dongyang.android.pcheduler.databinding.ItemListParentBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description : 할 일 목록 어댑터, ListAdapter 로 구성하였음.
 */

class TaskListAdapter(
    private val listViewModel: ListViewModel
) : ListAdapter<TaskItem, TaskListAdapter.TaskListViewHolder>(myDiffCallBack){

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            TaskItem.Parent.VIEW_TYPE -> TaskParentHolder(itemView)
            TaskItem.Child.VIEW_TYPE -> TaskChildHolder(parent.context, itemView, listViewModel)
            else -> throw IllegalArgumentException("Cannot create ViewHolder for view type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
//        Log.d(TAG, "onBindViewHolder position: ${getItem(position).task.id}")
        holder.bind(getItem(position))
    }

    abstract class TaskListViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: TaskItem)
    }

    companion object {
        const val TAG = "TaskListAdapter"
        val myDiffCallBack = object : DiffUtil.ItemCallback<TaskItem>() {
            override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}