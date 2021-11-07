package com.dongyang.android.pcheduler.Adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-06
 * @Description : 리사이클러뷰의 간격 조정을 돕는 클래스
 */

// 리사이클러뷰 간격 조정
class RecyclerViewDecoration(private val height: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = height
    }
}