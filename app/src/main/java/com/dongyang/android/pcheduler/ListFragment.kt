package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.Adapter.ListParentAdapter
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.FragmentListBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-31
 * @Description : 유저의 할일을 목록으로 볼 수 있는 화면
 */

@SuppressLint("StaticFieldLeak")
class ListFragment : Fragment() {

    var taskDateList = listOf<String>()
    lateinit var db : ListDatabase // 데이터베이스
    private var _binding : FragmentListBinding? = null // 뷰 바인딩
    // _를 붙이는 이유 : private 한 변수는 관례상 prefix 를 붙이는 경우가 많다.
    private val binding get()  = _binding!!
    // NULL able 이면 매번 ?. ?. 를 붙여야 하기에 NON-NULL 타입으로 쓰기 위해 한번 더 포장한다.

    private val listViewModel : ListViewModel by viewModels() // 뷰모델 연결
    private val parentAdapter : ListParentAdapter by lazy { ListParentAdapter(requireContext(), listViewModel)}


    var today = ""



    /*
         lateinit -> 전역변수 선언 후 null 값을 지정하지 않고 초기화
         var 키워드만 사용 가능하며, 기본 자료 타입(Int, Boolean, Double . .)에선 사용이 불가능하다.
         getter & setter 지정도 불가능함.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // View Binding -> FindViewById 없이 View에서 바로 사용할 수 있게 해줌.
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root



        // Context -> Fragment 에서는 requireContext() 를 사용한다.
        db = ListDatabase.getInstance(requireContext())!! // NOT NULL

        return view
    }

    @SuppressLint("ClickableViewAccessibility", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO : 클릭 시 오늘의 날짜 설정하게 변경하기(11/06)
        var currentTime : Long = System.currentTimeMillis()
        var timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko","KR"))
        today = timeFormat.format(Date(currentTime))

         // 코틀린에서 apply 변수로 가독성 좋게 표현할 수 있음.
        binding.listRcview.apply {
            // this.addItemDecoration(dividerItemDecoration)
            this.layoutManager = LinearLayoutManager(requireContext())
            this.addItemDecoration(RecyclerViewDecoration(30))
            this.adapter = parentAdapter
        }

//        listViewModel.currentData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//
//        })

        // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
        listViewModel.readParentData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            parentAdapter.setParent(it)
        })

//        getTask() // 최초 화면 돌입 시 할일 리스트 새로고침

        // TODO : 해당 날짜로 스크롤 내려가게 설정하기 (11/06)
/*
        for (i in taskDateList.indices) {
            if(taskDateList[i] == "2021-11-07") {
                binding.listRcview.scrollToPosition(i)
                break
            }
        }
*/

        binding.listBtnAdd.setOnClickListener{
            var text = binding.listEdtTask.text.toString() // 할 일 내용

            if (text == "") {
                Toast.makeText(requireContext(), "내용을 입력해주세요.",Toast.LENGTH_SHORT).show()
            } else {
                var task = TaskEntity(null, today,"",text,"NO","NO","")
                insertTask(task)

                binding.listEdtTask.setText("")
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


//    // RecyclerView 설정
//    fun setRecyclerView(taskDateList : List<String>) {
//        binding.listRcview.adapter = ListParentAdapter(requireContext(), taskDateList, this, db)
//    }


    // TODO : AsyncTask -> Coroutine 변환 (10/24 김정호)

    /**
     * 안드로이드에서는 Lint를 통해 성능상 문제가 있을 수 있는 코드를 관리한다.
     * 밑의 코드에서는 AsyncTask 때문에 메모리 누수가 일어날 수 있는데, SuppressLint 를 통해 경고를 무시할 수 있다.
     */

    fun insertTask(task : TaskEntity) {

        val getInsertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                // WorkerThread 에서 어떤 일을 할지 정의한다.
                listViewModel.insertTask(task)
            }


            override fun onPostExecute(result: Unit?) {
                // doInBackground 이후 어떤 일을 할 것인지 지정한다.
                super.onPostExecute(result)
            }

        }
        getInsertTask.execute()

    }
//
//    fun getTask() {
//        val getTimeTask = object : AsyncTask<Unit, Unit, Unit>() {
//            override fun doInBackground(vararg p0: Unit?) {
//
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//                setRecyclerView(listViewModel.readParentData)
//            }
//        }
//        getTimeTask.execute()
//    }
//
//    fun deleteTask(task: TaskEntity) {
//        val deleteTask = object : AsyncTask<Unit, Unit, Unit>() {
//            override fun doInBackground(vararg p0: Unit?) {
//                db.listDAO().deleteTask(task)
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//                getTask()
//            }
//        }
//        deleteTask.execute()
//    }
//
//    override fun onDeleteListener(task: TaskEntity) {
//        deleteTask(task)
//    }

    // 리사이클러뷰 간격 조정
    inner class RecyclerViewDecoration(private val height : Int) : RecyclerView.ItemDecoration() {
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