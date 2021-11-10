package com.dongyang.android.pcheduler

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.databinding.BottomsheetDetailBinding
import com.dongyang.android.pcheduler.databinding.DialogCalendarBinding
import com.dongyang.android.pcheduler.databinding.DialogDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-09
 * @Description : 할 일을 눌렀을 때 나오는 바텀시트 클래스
 */

class DetailBottomSheet(content: String) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetDetailBinding
    private val content = content
    private lateinit var pickDate : String
    private var onButton = ""



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



        binding.dbsEtContent.setText(content)

        val manager = (context as AppCompatActivity).supportFragmentManager
        val calendar = Calendar()
        var pickDate : String

        // FragmentResult ( Fragment <-> Fragment 통신)
        parentFragmentManager.setFragmentResultListener("requestKey", this) { resultKey, bundle ->
            val viewDate = bundle.getString("viewDate")
            pickDate = bundle.getString("pickDate")!!

            if (onButton == "start") {
                binding.dbsBtnStartDate.text = viewDate
            } else {
                binding.dbsBtnEndDate.text = viewDate
            }

            Log.d("result ::", viewDate.toString())
            Log.d("result ::", pickDate)
        }

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

                var pickDate = date.year.toString() + "-" + (date.month + 1).toString() + "-" + date.day.toString()
                // 데이터베이스에 넣을 값
                /*
                    pickDate.toString -> 2021.10.23일인데 2021-9-23 으로 결과값이 나오는 것을 발견하였음.
                    이유는, toString 으로 하면 Month가 배열로 취급받아서 index 값으로 나오기 때문임.
                 */

                var viewDate = (date.month + 1).toString() + "." + date.day.toString()
                // 뷰에 보여질 값

                // FragmentResult
                parentFragmentManager.setFragmentResult("requestKey", bundleOf(
                    "pickDate" to pickDate,
                    "viewDate" to viewDate)
                )

                dismiss()
            }
        }
    }
}