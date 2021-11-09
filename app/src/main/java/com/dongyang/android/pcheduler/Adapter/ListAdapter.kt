package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.DeleteListener
import com.dongyang.android.pcheduler.DetailBottomSheet
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.TabDialog
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.ItemListBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-30
 * @Description : 중첩 리사이클러뷰의 자식, 할 일의 구체적 내용을 담고 있음.
 */

class ListAdapter(
    val context: Context,
    var list: List<TaskEntity>,
    var onDeleteListener: DeleteListener,
    val db: ListDatabase
) : RecyclerView.Adapter<ListAdapter.MainViewHolder>(){




    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
//        val itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
//        return MainViewHolder(itemView)

        val binding = ItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = list[position]



        holder.taskText.text = task.content
        holder.taskDelete.setOnClickListener {
            onDeleteListener.onDeleteListener(task)
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
            val bottomSheet = DetailBottomSheet(task.content)
            bottomSheet.show(manager, "bottomSheet")
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
    }

    @SuppressLint("StaticFieldLeak")
    fun updateTask(task: TaskEntity) {
        val updateTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                // WorkerThread 에서 어떤 일을 할지 정의한다.
                db.listDAO().updateTask(task)
            }

            override fun onPostExecute(result: Unit?) {
                // doInBackground 이후 어떤 일을 할 것인지 지정한다.
                super.onPostExecute(result)
            }

        }
        updateTask.execute()
    }

    // 체크박스의 상태가 변경될 때 데이터베이스에 반영하고, 텍스트에 취소선을 긋는 함수
    private fun checkTask(task: TaskEntity, cb: CheckBox, tv: TextView) {
        cb.setOnCheckedChangeListener { button, ischecked ->
            if (ischecked) {
                task.complete = "OK"
                tv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                updateTask(task)
            } else {
                task.complete = "NO"
                tv.paintFlags = 0
                updateTask(task)
            }
        }
    }
}