package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.database.CategoryEntity
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.ItemListBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-30
 * @Description : 리스트 프래그먼트 리사이클러뷰의 어댑터
 */

class ListAdapter(
    val context: Context,
    var list: List<TaskEntity>,
    var onDeleteListener: DeleteListener,
    val db : ListDatabase
) : RecyclerView.Adapter<ListAdapter.MainViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
//        val itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
//        return MainViewHolder(itemView)

        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = list[position]


        holder.taskText.text = task.content

        holder.taskDelete.setOnClickListener {
            onDeleteListener.onDeleteListener(task)
        }

        holder.taskCheck.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                task.complete = "OK"
                updateTask(task)
                holder.taskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                task.complete = "NO"
                updateTask(task)
                holder.taskText.paintFlags = 0
            }
        }

        holder.taskText.paintFlags = when (task.complete) {
            "OK" -> Paint.STRIKE_THRU_TEXT_FLAG
            else -> 0
        }


    }


    inner class MainViewHolder(private val binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
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
                Log.d("ListAdapter", "저장 완료" + task.complete)
            }

        }
        updateTask.execute()
    }


}