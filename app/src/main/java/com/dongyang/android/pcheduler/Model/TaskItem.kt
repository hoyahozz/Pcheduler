package com.dongyang.android.pcheduler.Model

import com.dongyang.android.pcheduler.R

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description :
 */

sealed class TaskItem {
    abstract val task: TaskEntity
    abstract val layoutId: Int

    data class Parent(
        override val task: TaskEntity,
        override val layoutId: Int = VIEW_TYPE
    ) : TaskItem() {

        companion object {
            const val VIEW_TYPE = R.layout.item_list_parent
        }
    }

    data class Child(
        override val task: TaskEntity,
        override val layoutId: Int = VIEW_TYPE
    ) : TaskItem() {

        companion object {
            const val VIEW_TYPE = R.layout.item_list
        }
    }
}