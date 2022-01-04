package com.dongyang.android.pcheduler.Adapter

import android.content.Context
import android.graphics.Paint
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.UI.Dialog.DetailBottomSheet
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.ItemListBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description :
 */

class TaskChildHolder(
    private val context: Context,
    itemView: View,
    private val listViewModel: ListViewModel
) : TaskListAdapter.TaskListViewHolder(itemView) {
    private val binding by lazy { ItemListBinding.bind(itemView) }

    override fun bind(item: TaskItem) {
        val task = (item as TaskItem.Child).task
        binding.apply {

            val taskText = this.itemList
            val taskImage = this.itemListImg
            val taskContainer = this.itemListContainer
            val taskDelete = this.itemListDelete
            val taskCheck = this.itemListCheckBox
            val taskEndTime = this.itemListEndTime

            taskText.text = task.content

            taskDelete.setOnClickListener {
                listViewModel.deleteTask(task)
            }
            // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
            checkTask(task, taskCheck, taskText)

            // 리스트 불러올 때 취소선 및 체크박스 상태 설정
            taskCheck.isChecked = task.complete == "OK"
            taskText.paintFlags = when (task.complete) {
                "OK" -> Paint.STRIKE_THRU_TEXT_FLAG
                else -> 0
            }

            val manager = (context as AppCompatActivity).supportFragmentManager
            taskContainer.setOnClickListener {
//            TabDialog().show(manager, "TabDialog")
                val bottomSheet = DetailBottomSheet(task, listViewModel)
                bottomSheet.show(manager, bottomSheet.tag)
            }


            if (task.end_time != "") {
                val taskET = "~ " + task.end_time.substring(5, task.end_time.length)
                taskEndTime.text = taskET
                taskEndTime.visibility = View.VISIBLE
            } else { // 조건상 없을 때에는 UI를 다시 초기화 해주어야 하는 모습 확인
                taskEndTime.text = " "
                taskEndTime.visibility = View.INVISIBLE
            }


            if (task.alarm != "") {
                taskImage.setImageResource(R.drawable.ic_alarm_on)
            } else { // 조건상 없을 때에는 UI를 다시 초기화 해주어야 하는 모습 확인
                taskImage.setImageResource(R.drawable.ic_alarm_off)
            }
        }
    }

    // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
    private fun checkTask(task: TaskEntity, cb: CheckBox, tv: TextView) {

        cb.setOnClickListener {
            if (cb.isChecked) {
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
}