package com.dongyang.android.pcheduler.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-24
 * @Description : 카테고리 테이블을 정의하는 데이터 클래스
 */


// 카테고리 테이블
@Entity(tableName = "category") // 테이블 이름 지정
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) // 자동으로 증가
    @ColumnInfo(defaultValue = "1")
    var id: Int?,
    @ColumnInfo(defaultValue = "DEFAULT")
    var type: String = ""
)






