package com.dongyang.android.pcheduler.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.ViewModel.ListViewModel

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
        Log.d(TAG, "onCreateViewHolder: ON")
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

    // 화면에서 안보이는 리스트는 스와이프를 초기화
    override fun onViewDetachedFromWindow(holder: TaskListViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as RecyclerView.ViewHolder).itemView.animate().translationX(0f).setDuration(100L).start()
    }

//    override fun submitList(list: List<TaskItem>?) {
//        super.submitList(list?.let { ArrayList(it)})
//    }

    abstract class TaskListViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: TaskItem)
    }

    companion object {
        const val TAG = "TaskListAdapter"
        val myDiffCallBack = object : DiffUtil.ItemCallback<TaskItem>() {
            // 현 리스트에 노출하고 있는 아이템과 새로운 아이템이 서로 같은지 비교한다. (보통 고유한 ID값 체크)
            override fun areItemsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return oldItem.task.id == newItem.task.id
            }
            // 현 리스트에 노출하고 있는 아이템과 새로운 아이템의 equals 를 비교한다.
            override fun areContentsTheSame(oldItem: TaskItem, newItem: TaskItem): Boolean {
                return false
                // return oldItem == newItem

                /*
                    1. submitList() 를 통해 변경된 데이터 전달
                    2. areContentsTheSame 에서 oldItem.title - newItem.title 을 비교
                    3. 그런데 oldItem 에 새로 넣은 데이터가 들어가있어 UI 수정이 이루어지지 않음 (olditem == newitem) -> true

                    그래서 한 조치 -> 변경이 없어도 그냥 False 로 조치해버림. (성능 저하 가능성 있음.)
                 */
            }
        }
    }
}
