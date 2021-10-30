package com.dongyang.android.pcheduler.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-30
 * @Description : 할 일 테이블을 정의하는 데이터 클래스
 */

// 태스크 테이블
@Entity(
    tableName="task",
    foreignKeys = arrayOf(ForeignKey // 외래키 지정
        (entity = CategoryEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cid"),
        onDelete = ForeignKey.CASCADE)))
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,
    var cid : Int?,
    var content : String,
    @ColumnInfo(defaultValue = "NO")
    var favorite : String,
    @ColumnInfo(defaultValue = "NO")
    var complete : String,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var start_time : Long?,
    var end_time : Long?,
    @ColumnInfo(defaultValue = "NO")
    var alarm : String
)