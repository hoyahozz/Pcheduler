package com.dongyang.android.pcheduler.UI.Fragment

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Adapter.RecyclerViewDecoration
import com.dongyang.android.pcheduler.Adapter.SwipeHelperCallback
import com.dongyang.android.pcheduler.Adapter.TaskListAdapter
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.Model.TaskItem
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

    private lateinit var db: ListDatabase // 데이터베이스
    private var _binding: FragmentListBinding? = null // 뷰 바인딩

    // _를 붙이는 이유 : private 한 변수는 관례상 prefix 를 붙이는 경우가 많다.
    private val binding get() = _binding!!
    // NULL able 이면 매번 ?. ?. 를 붙여야 하기에 NON-NULL 타입으로 쓰기 위해 한번 더 포장한다.

    private val listViewModel: ListViewModel by viewModels()

    private val taskListAdapter by lazy {
        TaskListAdapter(listViewModel)
    }
    private var today = ""

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

        val currentTime: Long = System.currentTimeMillis()
        val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
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
        val swipeHelperCallback = SwipeHelperCallback("list").apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 9) // 1080 / 4 = 120
        }

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding.listRcview)


        // 코틀린에서 apply 변수로 가독성 좋게 표현
        binding.listRcview.apply {
            // this.addItemDecoration(dividerItemDecoration)
            this.adapter = taskListAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(RecyclerViewDecoration(15))

            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        listViewModel.readAllTask.observe(viewLifecycleOwner) { // 데이터에 변화가 있으면
            listViewModel.fetchTasks(it)
        }

        var prevListSize: Int = 0

        listViewModel.tasks.observe(viewLifecycleOwner) {
            taskListAdapter.submitList(it.toMutableList())

            /* TODO :: 새로운 데이터를 추가할 때 위로 스크롤이 올라가지 않는 상황 (원인 : UI 스레드가 데이터의 변화를 따라잡지 못하기 때문)
            * -> 이전 데이터 리스트와 현재 데이터 리스트의 사이즈를 비교해 더 커졌을 경우에 맨 위로 스크롤하게 설정하기 ?
            * -> 이전 데이터 리스트를 어떻게 구해올 것인지? 성능이 떨어지지 않을지?
            */

            val currentListSize = it.size
//            Log.d(TAG, "onViewCreated: prev size : $prevListSize - current size : $currentListSize")
            if (prevListSize < currentListSize) {
//                Log.d(TAG, "onViewCreated: 새 데이터 추가")
                GlobalScope.launch(Dispatchers.Main) { // 실행은 되지만, 글로벌 스코프를 끝내야 함
                    delay(200)
                    binding.listRcview.smoothScrollToPosition(0)
                }
            }

            prevListSize = it.size
        }

        binding.listBtnAdd.setOnClickListener {
            val text = binding.listEdtTask.text.toString() // 할 일 내용

            if (text == "") {
                Toast.makeText(requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val task = TaskEntity(null, today, "", text, "NO", "NO", "")
                listViewModel.insertTask(task)

                binding.listEdtTask.setText("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}