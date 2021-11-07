package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.DeleteListener
import com.dongyang.android.pcheduler.SwipeHelperCallback
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.ItemListParentBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-05
 * @Description : 중첩 리사이클러뷰의 부모, 날짜를 담는 역할을 수행함
 */

class ListParentAdapter(
    var context: Context,
    var list: List<String>,
    var onDeleteListener: DeleteListener,
    val db: ListDatabase
) : RecyclerView.Adapter<ListParentAdapter.ListParentViewHolder>() {

//    var childTask = listOf<TaskEntity>()
//    var childList = mutableListOf<List<TaskEntity>>()

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListParentViewHolder {
        val binding =
            ItemListParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListParentViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListParentViewHolder, position: Int) {
        val parentDate = list[position]

        Log.d("ParentDate :: ", parentDate)

        holder.date.text = parentDate

        val divideItemDecoration =
            DividerItemDecoration(holder.child.context, LinearLayoutManager(context).orientation)

        // ItemTouchHelper 를 RecyclerView 와 연결한다.
        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(200f)
        }

        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(holder.child)

        holder.child.apply {
            this.addItemDecoration(divideItemDecoration)
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(RecyclerViewDecoration(20))
            this.setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        getTask(holder.child, parentDate, context, holder.root)
    }

    @SuppressLint("StaticFieldLeak")
    fun getTask(recyclerView: RecyclerView, date: String, context: Context, constraintLayout: ConstraintLayout) {
        var getTask = object : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg p0: Unit?) {
                var childTask = db.listDAO().getChildtask(date)

                // setRecyclerView(...) -> Background 단계에서 RecyclerView 설정 X

                constraintLayout.post{
                    setRecyclerView(
                        childTask,
                        recyclerView,
                        context
                    )
                }

                // Main Thread 에서만 UI 수정이 가능하다. 고로, Main Thread를 호출해야 한다.
                // TODO : Thread, Run, Handler 공부하기 (11/06 김정호)

            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
//                for (i in childList.indices) {
//                    setRecyclerView(childList[i], recyclerView, context)
//                    // 자식 리사이클러뷰의 값이 모두 최근 데이터로 이루어지는 오류 발생
//               }

            }
        }
        getTask.execute()
    }

    fun setRecyclerView(task: List<TaskEntity>, recyclerView: RecyclerView, context: Context) {
        recyclerView.adapter = ListAdapter(context, task, onDeleteListener, db)
    }


    inner class ListParentViewHolder(private val binding: ItemListParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var date = binding.itemParentDate
        var child = binding.itemParentRv
        var container = binding.itemParentContainer
        var root = binding.root
    }


}