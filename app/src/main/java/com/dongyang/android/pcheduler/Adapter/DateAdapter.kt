package com.dongyang.android.pcheduler.Adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.UI.Dialog.DetailBottomSheet
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.ItemDateListBinding

class DateAdapter(
    private val viewModel : ListViewModel
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private val item = arrayListOf<TaskEntity>()

    fun submitList(task : List<TaskEntity>) {
        Log.d(TAG, "submitList: ON")
        this.item.clear()
        this.item.addAll(task)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemDateListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val task = item[position]
        Log.d(TAG, "onBindViewHolder: ${task.content}")

        holder.content.text = task.content

        // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
        checkTask(task, holder.check, holder.content)

        // 리스트 불러올 때 취소선 및 체크박스 상태 설정
        holder.check.isChecked = task.complete == "OK"
        holder.content.paintFlags = when (task.complete) {
            "OK" -> Paint.STRIKE_THRU_TEXT_FLAG
            else -> 0
        }

        if (task.end_time != "") {
            val taskET = "~ " + task.end_time.substring(5, task.end_time.length)
            holder.time.text = taskET
            holder.time.visibility = View.VISIBLE
        } else { // 조건상 없을 때에는 UI를 다시 초기화 해주어야 하는 모습 확인
            holder.time.text = " "
            holder.time.visibility = View.GONE
        }


        if (task.alarm != "") {
            holder.img.setImageResource(R.drawable.ic_alarm_on)
        } else { // 조건상 없을 때에는 UI를 다시 초기화 해주어야 하는 모습 확인
            holder.img.setImageResource(R.drawable.ic_alarm_off)
        }
    }

    override fun getItemCount(): Int = item.size

    inner class DateViewHolder(binding : ItemDateListBinding) : RecyclerView.ViewHolder(binding.root) {
        val content = binding.itemList
        val check = binding.itemListCheckBox
        val time = binding.itemListEndTime
        val img = binding.itemListImg


    }

    // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
    private fun checkTask(task: TaskEntity, cb: CheckBox, tv: TextView) {

        cb.setOnClickListener {
            if (cb.isChecked) {
                task.complete = "OK"
                tv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                viewModel.updateTask(task)
            } else {
                task.complete = "NO"
                tv.paintFlags = 0
                viewModel.updateTask(task)
            }
        }
    }


    companion object {
        private const val TAG = "DateAdapter"
    }

}