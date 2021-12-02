package com.dongyang.android.pcheduler.ViewModel

import android.app.Application
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
class ListViewModel() : ViewModel() {

    val readParentData: LiveData<List<String>>
    val parentData: List<String>
    private val repository: ListRepository

    // 초기값 설정
    init {
        val listDAO = ListDatabase.INSTANCE!!.listDAO()
        repository = ListRepository(listDAO)
        readParentData = repository.readParentData
        parentData = repository.readParentDate
    }

    private val taskLiveData = MutableLiveData<List<TaskItem>>()
    val tasks: LiveData<List<TaskItem>> get() = taskLiveData

    fun fetchTasks() {
        viewModelScope.launch {
            Log.d("task Test :::" , "viewmodel OK")
            val listItems = repository.readAllData.toListItems()
            taskLiveData.postValue(listItems)
        }
    }

    private fun List<TaskEntity>.toListItems(): List<TaskItem> {
        val result = arrayListOf<TaskItem>()
        var groupParentDate = ""
        this.forEach { task ->
            Log.d("task Test :::", "date :: $groupParentDate")
            if (groupParentDate != task.start_time) {
                result.add(TaskItem.Parent(task))
            }
            result.add(TaskItem.Child(task))
            groupParentDate = task.start_time
        }
        return result
    }

    fun insertTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        repository.updateTask(task)
    }

    fun deleteTask(task: TaskEntity) {
        repository.deleteTask(task)
    }

    fun getChildTask(start_time: String): List<TaskEntity> {
        val tmp = repository.readChildData(start_time)
        // _currentData.postValue(tmp)
        return tmp
    }

//    class Factory(val application: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return ListViewModel(application) as T
//        }
//    }

}