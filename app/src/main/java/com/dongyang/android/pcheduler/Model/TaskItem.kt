package com.dongyang.android.pcheduler.Model

import com.dongyang.android.pcheduler.R

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-12-02
 * @Description : ListAdapter 에 사용할 부모 - 자식 데이터 설정
 */

sealed class TaskItem {
    abstract val task: TaskEntity
    abstract val layoutId: Int

    data class Parent( // 날짜
        override val task: TaskEntity,
        override val layoutId: Int = VIEW_TYPE
    ) : TaskItem() {
        companion object {
            const val VIEW_TYPE = R.layout.item_list_parent
        }
    }
    data class Child( // 아이템
        override val task: TaskEntity,
        override val layoutId: Int = VIEW_TYPE
    ) : TaskItem() {

        companion object {
            const val VIEW_TYPE = R.layout.item_list
        }
    }
}
