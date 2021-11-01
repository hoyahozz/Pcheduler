package com.dongyang.android.pcheduler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
    var onDeleteListener: DeleteListener
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
        holder.taskContainer.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onDeleteListener.onDeleteListener(task)
                return true
            }
        })

    }


    inner class MainViewHolder(private val binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        var taskText = binding.itemList
        var taskImage = binding.itemListImg
        var taskContainer = binding.itemListContainer
    }


}