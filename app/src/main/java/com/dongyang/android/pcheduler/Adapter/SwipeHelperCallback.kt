package com.dongyang.android.pcheduler.Adapter

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.R
import kotlin.math.max
import kotlin.math.min

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-02
 * @Description : 리사이클러뷰에 Swipe 기능을 추가하여 드래그, 드랍할 수 있게 도와주는 유틸리티 클래스
 * 어떻게 움직이느냐에 따라 이벤트를 수신하고, 그에 따른 동작을 개발자가 설정할 수 있다.
 */

// ItemTouchHelper Callback 클래스 구현
class SwipeHelperCallback(
    private val type : String
) : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f // 현재 x값
    private var clamp = 0f // 고정시킬 크기

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
        currentDx = 0f // 현재 x 위치 초기화
        previousPosition = viewHolder.adapterPosition // 드래그, 스와이프 동작이 끝나는 view의 포지션을 받아온다.
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    // ViewHolder 를 스와이프하거나 드래그 했을 때 호출한다.
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition // 현재 드래그, 스와이프 중인 view의 포지션을 받아온다.
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

    // 사용자가 손을 떼면 호출됨
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
         val isClamped = getTag(viewHolder)
         setTag(viewHolder, !isClamped && currentDx <= -clamp)
        // 현재 View 가 고정되어 있지 않고 사용자가 -clamp 이상 스와이프할 시 isClamped를 true로 변경한다.
        // setTag(viewHolder, currentDx <= -clamp)
        return 2f
    }


    // 아이템 터치 혹은 스와이프와 같이 뷰에 변화가 생길 경우 호출하는 함수
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder) // 고정 여부 결정 (true -> 고정, false -> 고정 X)
            val newX = clampViewPositionHorizontal(
                view,
                dX,
                isClamped,
                isCurrentlyActive
            ) // x만큼 이동(고정 시 이동 위치, 고정 해g 시 이동 위치 결정)

            // 고정 시킬 때 애니메이션 추가
            if (newX == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = newX
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, newX, dY, actionState, isCurrentlyActive
            )
        }
    }

    // Swipe 했을 때 삭제 화면이 보이도록 고정한다.
    private fun clampViewPositionHorizontal(
        view : View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ): Float {
//         View 의 가로 길이 절반까지만 스와이프 되도록 설정한다.
        val min: Float = -view.width.toFloat() / 4
        // RIGHT 방향으로 스와이프를 막는다.
        val max = 0f

        // 고정할 수 있으면
//        val newX = if (isClamped) {
//            // 현재 swipe 중이면 swipe되는 영역 제한
//            if (isCurrentlyActive)
//            // 오른쪽 swipe일 때
//                if (dX < 0) dX / 3 - clamp
//                // 왼쪽 swipe일 때
//                else dX - clamp
//            // swipe 중이 아니면 고정시키기
//            else -clamp
//        }
//        // 고정할 수 없으면 newX는 스와이프한 만큼
//        else dX
//
//        // newX가 0보다 작은지 확인
//        return min(newX, max)

        val x = if (isClamped) {
            // View 고정 시 스와이프 되는 영역을 제한한다.
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }


    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    // 다른 View가 스와이프되거나 터치되면 고정을 해제한다.
    fun removePreviousClamp(recyclerView: RecyclerView) {

        Log.d(TAG, "removePreviousClamp: $previousPosition :: $currentPosition")
        // 현재 선택한 view가 이전에 선택한 view와 같으면 넘어감
        if (currentPosition == previousPosition) return

        // 이전에 선택한 위치의 view 고정 해제
        previousPosition?.let { // 이전 포지션 값이 있으면 레이아웃을 0f 로 조정

            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).animate().translationX(0f).setDuration(100L).start()
            setTag(viewHolder, false)
            previousPosition = null
        }
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {

        Log.d("type Test", "getView: $type")
        return if(type == "list") {
            viewHolder.itemView.findViewById(R.id.item_list_container)
        } else {
            viewHolder.itemView.findViewById(R.id.item_date_list_container)
        }

    }

    // isClamped 를 View의 Tag로 관리한다.
    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    // isClamped 를 View의 Tag로 관리한다.
    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }


    companion object {
        private const val TAG = "SwipeHelperCallback"
    }
}