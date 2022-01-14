package com.dongyang.android.pcheduler.UI.Fragment

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Adapter.SwipeHelperCallback
import com.dongyang.android.pcheduler.Adapter.TaskAdapter
import com.dongyang.android.pcheduler.Adapter.TaskListAdapter
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.Model.TaskItem
import com.dongyang.android.pcheduler.Repository.ListRepository
import com.dongyang.android.pcheduler.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-31
 * @Description : 유저의 할일을 목록으로 볼 수 있는 화면
 */

class ListFragment : Fragment() {

    lateinit var db: ListDatabase // 데이터베이스
    private var _binding: FragmentListBinding? = null // 뷰 바인딩

    // _를 붙이는 이유 : private 한 변수는 관례상 prefix 를 붙이는 경우가 많다.
    private val binding get() = _binding!!
    // NULL able 이면 매번 ?. ?. 를 붙여야 하기에 NON-NULL 타입으로 쓰기 위해 한번 더 포장한다.

    private val listViewModel: ListViewModel by viewModels()
    private val taskAdapter by lazy {
        TaskAdapter(listViewModel)
    }
    private val taskListAdapter by lazy {
        TaskListAdapter(listViewModel)
    }
    var today = ""

    /*
         lateinit -> 전역변수 선언 후 null 값을 지정하지 않고 초기화
         var 키워드만 사용 가능하며, 기본 자료 타입(Int, Boolean, Double . .)에선 사용이 불가능하다.
         getter & setter 지정도 불가능함.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // View Binding -> FindViewById 없이 View에서 바로 사용할 수 있게 해줌.
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        // Context -> Fragment 에서는 requireContext() 를 사용한다.
        db = ListDatabase.getInstance(requireContext())!! // NOT NULL

        var currentTime: Long = System.currentTimeMillis()
        var timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        today = timeFormat.format(Date(currentTime))

        return view
    }

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : 해당 날짜로 스크롤 내려가게 설정하기 (11/06)
/*
        for (i in taskDateList.indices) {
            if(taskDateList[i] == "2021-11-07") {
                binding.listRcview.scrollToPosition(i)
                break
            }
        }
*/

        // TODO :: ItemTouchHelper
        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(100f)
        }

        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.listRcview)



        // 코틀린에서 apply 변수로 가독성 좋게 표현할 수 있음.
        binding.listRcview.apply {
            // this.addItemDecoration(dividerItemDecoration)
            this.adapter = taskListAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(RecyclerViewDecoration(5))

            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        listViewModel.readAllTask.observe(viewLifecycleOwner) { // 데이터에 변화가 있으면
            Log.d("task test", "All task observing")
            listViewModel.fetchTasks(it)
        }

        listViewModel.tasks.observe(viewLifecycleOwner) {
            Log.d("task test", "Tasks Observing")
            taskListAdapter.submitList(it.toMutableList())
        }

        binding.listBtnAdd.setOnClickListener {
            var text = binding.listEdtTask.text.toString() // 할 일 내용

            if (text == "") {
                Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                var task = TaskEntity(null, today, "", text, "NO", "NO", "")
                listViewModel.insertTask(task)

                binding.listEdtTask.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 리사이클러뷰 간격 조정
    inner class RecyclerViewDecoration(private val height: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = height
        }
    }
}