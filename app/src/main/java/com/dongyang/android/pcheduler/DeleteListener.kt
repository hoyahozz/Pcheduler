package com.dongyang.android.pcheduler

import com.dongyang.android.pcheduler.Model.TaskEntity

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-02
 * @Description : 목록 삭제 리스너
 */


// TODO : 짧은 코드가 파일 하나를 잡아먹는 것이 불-편하니 더 좋은 방법 모색해보기 (11/02 김정호)

interface DeleteListener {
    fun onDeleteListener(task: TaskEntity)
}