package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.BottomsheetDetailBinding
import com.dongyang.android.pcheduler.databinding.DialogCalendarBinding
import com.dongyang.android.pcheduler.databinding.DialogDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-09
 * @Description : 할 일을 눌렀을 때 나오는 바텀시트 클래스
 */

class DetailBottomSheet(task: TaskEntity) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetDetailBinding
    private lateinit var db: ListDatabase
    private val task = task
    private lateinit var pickDate: String
    private var onButton = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomsheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var start_time = ""
        var end_time = ""

        // FragmentResult ( Fragment <-> Fragment 통신)
        parentFragmentManager.setFragmentResultListener("requestKey", this) { resultKey, bundle ->
            val viewDate = bundle.getString("viewDate")
            pickDate = bundle.getString("pickDate")!!

            if (onButton == "start") {
                binding.dbsBtnStartDate.text = viewDate
                task.start_time = pickDate
            } else {
                binding.dbsBtnEndDate.text = viewDate
                task.end_time = pickDate
            }

            Log.d("result ::", viewDate.toString())
            Log.d("result ::", pickDate)
        }

        // 시작 시간, 종료 시간이 널이 아니라면 버튼에 값 넣기
        task.start_time.let {
            start_time = task.start_time.substring(5, task.start_time.length) // 시작 버튼에 들어갈 값
            binding.dbsBtnStartDate.text = start_time
        }

        task.end_time.let {
            if (task.end_time != "") {
                end_time = it.substring(5, task.end_time.length)
                binding.dbsBtnEndDate.text = end_time
            }
        }



        binding.dbsEtContent.setText(task.content)
        db = ListDatabase.getInstance(requireContext())!! // DB

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.dbsEtContent.requestFocus() // EditText에 포커스를 올림
        imm.showSoftInput(binding.dbsEtContent, 0)


        binding.dbsEtContent.imeOptions = EditorInfo.IME_ACTION_DONE // EditText 완료 버튼 설정
        // 완료 버튼을 눌렀을 때 할 행동 설정
        binding.dbsEtContent.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // TODO : 완료 버튼을 눌렀을 때 데이터베이스 수정 일어나게 설정하기 (11/13)
                task.content = binding.dbsEtContent.text.toString()
                updateTask(task)

                Log.d("Update Test :: ", task.start_time + " + " + task.end_time)

                dismiss()
                true
            }
            false
        }


        val manager = (context as AppCompatActivity).supportFragmentManager
        val calendar = Calendar()

        // 시작 버튼을 눌렀을 때 달력 출력되게 설정하였음.
        binding.dbsBtnStartDate.setOnClickListener {
            onButton = "start"
            calendar.show(parentFragmentManager, "CalendarView")
        }

        binding.dbsBtnEndDate.setOnClickListener {
            onButton = "end"
            Calendar().show(manager, "CalendarView")
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun updateTask(task: TaskEntity) {
        val updateTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                // WorkerThread 에서 어떤 일을 할지 정의한다.
                db.listDAO().updateTask(task)
            }

            override fun onPostExecute(result: Unit?) {
                // doInBackground 이후 어떤 일을 할 것인지 지정한다.
                super.onPostExecute(result)
            }
        }
        updateTask.execute()
    }


    // TODO : Inner class로 호출 시 오류, 원인 알아보기(11/10)
    // TODO : 종료 날짜가 시작 날짜보다 멀어야 함.
    class Calendar : DialogFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // 화면 밖을 누르면 dismiss 되게 설정
            isCancelable = true
        }

        private lateinit var dialogBinding: DialogCalendarBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            dialogBinding = DialogCalendarBinding.inflate(inflater, container, false)
            return dialogBinding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // 날짜를 선택했을 때 나타나는 이벤트 리스너
            // date -> CalendarDay(2021-10-23)
            dialogBinding.dialogDatePicker.setOnDateChangedListener { widget, date, selected ->

                var pickDate =
                    date.year.toString() + "-" + (date.month + 1).toString() + "-" + date.day.toString()
                // 데이터베이스에 넣을 값
                /*
                    pickDate.toString -> 2021.10.23일인데 2021-9-23 으로 결과값이 나오는 것을 발견하였음.
                    이유는, toString 으로 하면 Month가 배열로 취급받아서 index 값으로 나오기 때문임.
                 */

                var viewDate = (date.month + 1).toString() + "-" + date.day.toString()
                // 뷰에 보여질 값

                // FragmentResult
                parentFragmentManager.setFragmentResult(
                    "requestKey", bundleOf(
                        "pickDate" to pickDate,
                        "viewDate" to viewDate
                    )
                )
                dismiss()
            }
        }
    }
}




