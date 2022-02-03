package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.*
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.UI.Dialog.DetailBottomSheet
import com.dongyang.android.pcheduler.databinding.ItemListBinding


/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-30
 * @Description : 중첩 리사이클러뷰의 자식, 할 일의 구체적 내용을 담고 있음.
 */

class ListChildAdapter(
    val context: Context,
    private val listViewModel: ListViewModel,
    private var childList : List<TaskEntity>
) : RecyclerView.Adapter<ListChildAdapter.MainViewHolder>() {

//    private var childList = emptyList<TaskEntity>()


    override fun getItemCount(): Int {
        return childList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
//        val itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
//        return MainViewHolder(itemView)

        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = childList[position]

        holder.taskText.text = task.content
        holder.taskDelete.setOnClickListener {
            listViewModel.deleteTask(task)
        }

        // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
        checkTask(task, holder.taskCheck, holder.taskText)

        // 리스트 불러올 때 취소선 및 체크박스 상태 설정
        holder.taskCheck.isChecked = task.complete == "OK"
        holder.taskText.paintFlags = when (task.complete) {
            "OK" -> Paint.STRIKE_THRU_TEXT_FLAG
            else -> 0
        }

        val manager = (context as AppCompatActivity).supportFragmentManager
        holder.taskContainer.setOnClickListener {
//            TabDialog().show(manager, "TabDialog")
            val bottomSheet = DetailBottomSheet(task, listViewModel)
            bottomSheet.show(manager, bottomSheet.tag)
        }

        if (task.end_time != "") {
            val taskET = "~ " + task.end_time.substring(5, task.end_time.length)
            holder.taskEndTime.text = taskET
            holder.taskEndTime.visibility = View.VISIBLE
        }

        if (task.alarm != "") {
            holder.taskImage.setImageResource(R.drawable.ic_alarm_on)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class MainViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        var taskText = binding.itemList
        var taskImage = binding.itemListImg
        var taskContainer = binding.itemListContainer
        var taskDelete = binding.itemListDelete
        var taskCheck = binding.itemListCheckBox
        var taskEndTime = binding.itemListEndTime
    }

    // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
    private fun checkTask(task: TaskEntity, cb: CheckBox, tv: TextView) {
        cb.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                task.complete = "OK"
                tv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                listViewModel.updateTask(task)
            } else {
                task.complete = "NO"
                tv.paintFlags = 0
                listViewModel.updateTask(task)
            }
        }
    }

//    fun setChild(task: List<TaskEntity>) {
//        childList = task
//        notifyDataSetChanged()
//    }
}