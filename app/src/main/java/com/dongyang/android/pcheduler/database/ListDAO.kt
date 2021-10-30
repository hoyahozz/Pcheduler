package com.dongyang.android.pcheduler.database

import androidx.room.*

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

    // Category Table CRD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category : CategoryEntity)

    @Query("SELECT * FROM category")
    fun getCategory() : List<CategoryEntity>

    @Delete
    fun deleteCategory(category : CategoryEntity)

    // Task Table CRD

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task : TaskEntity)

    @Query("SELECT * FROM task")
    fun getTask() : List<TaskEntity>

    @Delete
    fun deleteTask(task : TaskEntity)
    
}