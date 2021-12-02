package com.dongyang.android.pcheduler.Adapter

import android.view.View
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.databinding.ItemListParentBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description :
 */

class TaskParentHolder(
    itemView: View
) : TaskAdapter.TaskViewHolder(itemView) {
    private val binding by lazy { ItemListParentBinding.bind(itemView) }

    override fun bind(item: TaskItem) {
        val task = (item as TaskItem.Parent).task
        binding.apply {
            this.itemParentDate.text = task.start_time
        }
    }
}