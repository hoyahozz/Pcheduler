package com.dongyang.android.pcheduler

import androidx.lifecycle.LiveData
import com.dongyang.android.pcheduler.database.ListDAO
import com.dongyang.android.pcheduler.database.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-22
 * @Description :
 */

class ListRepository(private val listDAO : ListDAO) {
    val readParentData : LiveData<List<String>> = listDAO.getParentTask()

    fun readChildData(start_time : String) : List<TaskEntity> {
        return listDAO.getChildTask(start_time)
    }

    fun insertTask(task : TaskEntity) {
        listDAO.insertTask(task)
    }

    fun deleteTask(task : TaskEntity) {
        listDAO.deleteTask(task)
    }

    fun updateTask(task : TaskEntity) {
        listDAO.updateTask(task)
    }
}