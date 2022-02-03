package com.dongyang.android.pcheduler.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.dongyang.android.pcheduler.Repository.ListRepository
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.Model.TaskItem
import kotlinx.coroutines.launch

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-22
 * @Description :
 */

// 뷰모델은 DB에 직접 접근 X
class ListViewModel : ViewModel() {

    private val readParentData: LiveData<List<String>>
    val readAllTask: LiveData<List<TaskEntity>>
    private val repository: ListRepository
    private var _currentData = MutableLiveData<List<TaskEntity>>()
    val currentData: LiveData<List<TaskEntity>>
        get() = _currentData

    // 초기값 설정
    init {
        val listDAO = ListDatabase.INSTANCE!!.listDAO()
        repository = ListRepository(listDAO)
        readParentData = repository.readParentData
        readAllTask = repository.readAllData
    }

    private val taskLiveData = MutableLiveData<List<TaskItem>>() // 변화가능한 라이브데이터
    val tasks: LiveData<List<TaskItem>> get() = taskLiveData // getter

    fun fetchTasks(task: List<TaskEntity>) {
        viewModelScope.launch {
            val listItems = task.toListItems()
            taskLiveData.postValue(listItems)
        }
    }

    private fun List<TaskEntity>.toListItems(): List<TaskItem> { // Kotlin extension
        // 부모 날짜별로 데이터를 정리
        val result = arrayListOf<TaskItem>()
        var groupParentDate = ""
        this.forEach { task ->
            // 날짜가 달라지면 그룹헤더 추가
            if (groupParentDate != task.start_time) {
                result.add(TaskItem.Parent(task))
            }
            result.add(TaskItem.Child(task)) // 태스크 추가
            groupParentDate = task.start_time // 그룹 날짜를 바로 이전 날짜로 설정
        }
        return result
    }

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun readDateData(start_time: String) {
        viewModelScope.launch {
            val tmp = repository.readDateTask(start_time)
            _currentData.postValue(tmp)
        }
    }

    private val _currentTask = MutableLiveData<TaskEntity>()
    val currentTask : LiveData<TaskEntity> get() = _currentTask

    fun getTask(id: Int) {
        Log.d("getTask", "ON")
        viewModelScope.launch {
            val tmp = repository.getTask(id)
            _currentTask.postValue(tmp)
        }
    }
}