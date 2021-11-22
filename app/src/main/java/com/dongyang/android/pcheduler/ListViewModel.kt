package com.dongyang.android.pcheduler

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-22
 * @Description :
 */

// 뷰모델은 DB에 직접 접근 X
class ListViewModel(application : Application) : AndroidViewModel(application){

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

    fun readChildData(start_time : String) {
        val tmp = repository.readChildData(start_time)
        _currentData.postValue(tmp)
    }

}