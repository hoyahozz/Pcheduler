package com.dongyang.android.pcheduler.Adapter

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-02
 * @Description : 리사이클러뷰에 Swipe 기능을 추가하여 드래그, 드랍할 수 있게 도와주는 유틸리티 클래스
 * 어떻게 움직이느냐에 따라 이벤트를 수신하고, 그에 따른 동작을 개발자가 설정할 수 있다.
 */

// ItemTouchHelper Callback 클래스 구현
class SwipeHelperCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    // 이동 방향을 결정한다.
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is TaskParentHolder) { // 부모 태스크는 좌우로 스와이프 되지 않게 설정하였음.
            makeMovementFlags(0, 0)
        } else {
            makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
        }
    }

    // 드래그될 때 호출된다.
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    // 스와이프할 때 호출된다.
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    // View 전체가 Swipe 되는것이 아니라, 특정 View만 Swipe 되도록 설정하기
    // 기본적으로 ItemTouchHelper.Callback 에는 public static ItemTouchUIUtil getDefaultUIUtil()이 구현되어 있다.

    // 드래그된 View 가 Drop 되었거나 Swipe 가 Cancel 되거나 Complete 되었을 때 불린다.
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.adapterPosition // 몇번째 리스트인지 받아온다.
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
        /*
            ?. : 앞의 변수가 null이 아닐 때만 오른쪽 함수가 실행되고, null 이면 null return
            if (viewHolder != null) { ... } else null

            let : 지정된 값이 null이 아닌 경우에만 코드를 실행하는 경우 사용한다.
         */
    }

    /*
    기본적으로 일정 범위 밖이나 일정 속도 이상으로 스와이프할 시 View 밖으로
    escape 하도록 구현되어 있는 것을 막는 함수들이다.
    */
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // 현재 View 가 고정되어 있지 않고 사용자가 -clamp 이상 스와이프할 시 isClamped를 true로 변경한다.
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

            currentDx = x
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, x, dY, actionState, isCurrentlyActive
            )
        }
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
        // View 의 가로 길이 절반까지만 스와이프 되도록 설정한다.
        val min: Float = -view.width.toFloat() / 4
        // RIGHT 방향으로 스와이프를 막는다.
        val max: Float = 0f

        val x = if (isClamped) {
            // View 고정 시 스와이프 되는 영역을 제한한다.
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }

    // 다른 View가 스와이프되거나 터치되면 고정을 해제한다.
    fun removePreviousClamp(recyclerView: RecyclerView) {
        // Log.d(TAG, "removePreviousClamp: ON")
        if (currentPosition == previousPosition) return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder as TaskChildHolder).taskContainer
    }

    // isClamped 를 View의 Tag로 관리한다.
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    // isClamped 를 View의 Tag로 관리한다.
    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    companion object {
        private const val TAG = "SwipeHelperCallback"
    }
}