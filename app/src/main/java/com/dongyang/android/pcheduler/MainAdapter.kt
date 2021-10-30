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
 * @Description :
 */

class MainAdapter(
    val context: Context,
    var list: List<TaskEntity>
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


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
    }


    inner class MainViewHolder(private val binding : ItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        var taskText = binding.listEdt
        var taskImage = binding.listImg
    }

}