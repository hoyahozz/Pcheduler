package com.dongyang.android.pcheduler

import com.dongyang.android.pcheduler.database.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-02
 * @Description :
 */

interface DeleteListener {
    fun onDeleteListener(task: TaskEntity)
}