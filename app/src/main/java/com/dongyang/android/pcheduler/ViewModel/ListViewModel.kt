package com.dongyang.android.pcheduler.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dongyang.android.pcheduler.Repository.ListRepository
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-22
 * @Description :
 */

// 뷰모델은 DB에 직접 접근 X
class ListViewModel(application : Application) : AndroidViewModel(application){

    val readAllData : LiveData<List<TaskEntity>>
    val readParentData : LiveData<List<String>>
    private val repository : ListRepository

    // getter, setter
    private val _currentData = MutableLiveData<List<TaskEntity>>()
    val currentData : LiveData<List<TaskEntity>>
        get() = _currentData

    // 초기값 설정
    init {
        val listDAO = ListDatabase.INSTANCE!!.listDAO()
        repository = ListRepository(listDAO)
        readAllData = repository.readAllData
        readParentData = repository.readParentData
    }

    fun insertTask(task : TaskEntity) {
        repository.insertTask(task)
    }

    fun updateTask(task : TaskEntity) {
        repository.updateTask(task)
    }

    fun deleteTask(task: TaskEntity) {
        repository.deleteTask(task)
    }

    fun getChildTask(start_time : String) {
        var tmp = repository.readChildData(start_time)
        _currentData.postValue(tmp)
    }

}