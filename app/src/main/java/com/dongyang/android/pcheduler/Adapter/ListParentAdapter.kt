package com.dongyang.android.pcheduler.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.databinding.ItemListParentBinding

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-05
 * @Description : 중첩 리사이클러뷰의 부모, 날짜를 담는 역할을 수행함
 */

class ListParentAdapter(
    val context: Context,
    private val listViewModel: ListViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<ListParentAdapter.ListParentViewHolder>() {

    private var parentList = emptyList<String>()
    var testList = emptyList<TaskEntity>()
//    var childTask = listOf<TaskEntity>()
//    var childList = mutableListOf<List<TaskEntity>>()

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListParentViewHolder {
        val binding =
            ItemListParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListParentViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ListParentViewHolder, position: Int) {
        val parentDate = parentList[position]
        val childAdapter: ListChildAdapter by lazy { ListChildAdapter(context, listViewModel) }


        Log.d("ParentDate :: ", parentDate)

        holder.date.text = parentDate

        var divideItemDecoration =
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
            // this.addItemDecoration(RecyclerViewDecoration(20))
            this.setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
            this.adapter = childAdapter
        }

        test(parentDate, childAdapter)

//        listViewModel.readAllData.observe(lifecycleOwner) {
//            test(parentDate, childAdapter)
//        }


//
//        getTask(holder.child, parentDate, context, holder.root)
    }

//    @SuppressLint("StaticFieldLeak")
//    fun getTask(recyclerView: RecyclerView, date: String, context: Context, constraintLayout: ConstraintLayout) {
//        var getTask = object : AsyncTask<Unit, List<TaskEntity>, Unit>() {
//
//            override fun doInBackground(vararg p0: Unit?) {
//                var childTask = listViewModel.readChildData(date)
//                // publishProgress(childTask)
//
//
//                // setRecyclerView(...) -> Background 단계에서 RecyclerView 설정 X
//
////                constraintLayout.post{
////                    setRecyclerView(
////                        childTask,
////                        recyclerView,
////                        context
////                    )
////                }
//                // Main Thread 에서만 UI 수정이 가능하다. 고로, Main Thread를 호출해야 한다.
//                // TODO : Thread, Run, Handler 공부하기 (11/06 김정호)
//            }
//
//            override fun onProgressUpdate(vararg childTask: List<TaskEntity>?) {
//
//                val count = childTask.size
//
//                for (i in 0 until count) {
//                    childTask[i]?.let {
//                        setRecyclerView(
//                            it,
//                            recyclerView,
//                            context
//                        )
//                    }
//                }
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
////                for (i in childList.indices) {
////                    setRecyclerView(childList[i], recyclerView, context)
////                    // 자식 리사이클러뷰의 값이 모두 최근 데이터로 이루어지는 오류 발생
////               }
//
//            }
//        }
//        getTask.execute()
//    }

//    fun setRecyclerView(task: List<TaskEntity>, recyclerView: RecyclerView, context: Context) {
//        recyclerView.adapter = ListChildAdapter(listViewModel, context)
//    }

    @SuppressLint("StaticFieldLeak")
    fun test(parentDate: String, childAdapter: ListChildAdapter) {
        var testData : List<TaskEntity>? = null
        val childTask =
            object : AsyncTask<Unit, Unit, Unit>() {
                override fun doInBackground(vararg p0: Unit?) {
                    testData = listViewModel.getChildTask(parentDate)
                }

                override fun onProgressUpdate(vararg values: Unit?) {
                    super.onProgressUpdate(*values)
                }

                override fun onPostExecute(result: Unit?) {
                    super.onPostExecute(result)
                    testData?.let { childAdapter.setChild(it) }
                }

            }
        childTask.execute()
    }


    inner class ListParentViewHolder(private val binding: ItemListParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var date = binding.itemParentDate
        var child = binding.itemParentRv
        var container = binding.itemParentContainer
        var root = binding.root
    }

    fun setParent(task: List<String>) {
        parentList = task
        notifyDataSetChanged()
    }


}