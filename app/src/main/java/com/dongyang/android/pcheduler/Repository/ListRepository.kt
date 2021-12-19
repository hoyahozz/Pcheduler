package com.dongyang.android.pcheduler.Repository

import androidx.lifecycle.LiveData
import com.dongyang.android.pcheduler.Database.ListDAO
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-22
 * @Description :
 */

class ListRepository(private val listDAO : ListDAO) {

    // DAO를 접근

//    val readAllData : LiveData<List<TaskEntity>> = listDAO.getAllTask()
    val readAllData : LiveData<List<TaskEntity>> = listDAO.getAllTask()
    val readParentData : LiveData<List<String>> = listDAO.getParentTask()
    // val readParentDate : List<String> = listDAO.getParentDate()


    suspend fun insertTask(task : TaskEntity) {
        listDAO.insertTask(task)
    }

    suspend fun deleteTask(task : TaskEntity) {
        listDAO.deleteTask(task)
    }

    suspend fun updateTask(task : TaskEntity) {
        listDAO.updateTask(task)
    }

    fun readChildData(start_time : String) : List<TaskEntity> {
        return listDAO.getChildTask(start_time)
    }
}