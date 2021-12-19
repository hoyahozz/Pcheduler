package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.ItemListBinding
import com.dongyang.android.pcheduler.databinding.ItemListParentBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description :
 */

class TaskAdapter(
    context : Context,
    listViewModel: ListViewModel
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val items = arrayListOf<TaskItem>()
    private val listViewModel = listViewModel
    private val context = context

    private val swipeHelperCallback = SwipeHelperCallback().apply {
        setClamp(200f)
    }

    fun submitList(items: List<TaskItem>) { // 기존에 있던 데이터를 지우고 다시 데이터를 수정
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): TaskItem = this.items[position]

    override fun getItemCount(): Int{
        return this.items.size
    }

    override fun getItemViewType(position: Int): Int = getItem(position).layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            TaskItem.Parent.VIEW_TYPE -> TaskParentHolder(itemView)
            TaskItem.Child.VIEW_TYPE -> TaskChildHolder(context, itemView, listViewModel).apply {

            }
            else -> throw IllegalArgumentException("Cannot create ViewHolder for view type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract class TaskViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: TaskItem)
    }
}