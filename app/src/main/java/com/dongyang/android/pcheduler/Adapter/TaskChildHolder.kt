package com.dongyang.android.pcheduler.Adapter

import android.view.View
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.databinding.ItemListBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description :
 */

class TaskChildHolder(
    itemView : View
) : TaskAdapter.TaskViewHolder(itemView) {
    private val binding by lazy { ItemListBinding.bind(itemView) }

    override fun bind(item: TaskItem) {
        val task = (item as TaskItem.Child).task
        binding.apply {
            this.itemList.text = task.content
        }
    }
}