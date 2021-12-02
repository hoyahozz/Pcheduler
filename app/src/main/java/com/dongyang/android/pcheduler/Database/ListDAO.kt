package com.dongyang.android.pcheduler.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dongyang.android.pcheduler.Model.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-24
 * @Description : Data Access Object 를 정의하는 인터페이스
 */


@Dao
interface ListDAO {
    /**
     * onConflict = REPLACE
     * 기존의 기본키를 가진 값이 있는데, 나중에 해당 기본키에 다른 값을 넣으면,
     * 넣은 값으로 대체된다는 의미이다. 쉽게 이야기해서 덮어쓰기의 개념.
     */

    // Task Table CRD

    // onConflict (충돌이 일어났을 때 덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task : TaskEntity)

//    @Query("SELECT * FROM task")
//    fun getAllTask() : LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task ORDER BY start_time DESC")
    fun getAllTask() : List<TaskEntity>

    @Delete
    fun deleteTask(task : TaskEntity)

    @Update
    fun updateTask(task: TaskEntity)

    @Query("SELECT DISTINCT start_time FROM task ORDER BY start_time ASC")
    fun getParentTask() : LiveData<List<String>>

    @Query("SELECT DISTINCT start_time FROM task ORDER BY start_time ASC")
    fun getParentDate() : List<String>

    @Query("SELECT * FROM TASK WHERE start_time = :start_time")
    fun getChildTask(start_time : String) : List<TaskEntity>

}