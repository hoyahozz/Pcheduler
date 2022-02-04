package com.dongyang.android.pcheduler.UI.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.databinding.DialogCalendarBinding
import java.text.SimpleDateFormat

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-19
 * @Description : 캘린더 날짜 설정 다이얼로그
 */

// TODO : Inner class로 호출 시 오류, 원인 알아보기(11/10)
// TODO : 종료 날짜가 시작 날짜보다 멀어야 함.

class CalendarDialog : DialogFragment() {
    private lateinit var dialogBinding: DialogCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialogBinding = DialogCalendarBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialogBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = SimpleDateFormat("yyyy-MM-dd")

        dialogBinding.dialogDatePicker.state().edit()


        // 날짜를 선택했을 때 나타나는 이벤트 리스너
        // date -> CalendarDay(2021-10-23)
        dialogBinding.dialogDatePicker.setOnDateChangedListener { widget, date, selected ->
            // 0이 붙게 convert 한다.
            val month =
                if(date.month + 1 < 10) {
                    "0" + (date.month + 1).toString()
                } else {
                    (date.month + 1).toString()
                }
            val day =
                if(date.day < 10) {
                    "0" + date.day.toString()
                } else {
                    date.day.toString()
                }

            val pickDate =
                date.year.toString() + "-$month-$day"
            // 데이터베이스에 넣을 값
            /*
                pickDate.toString -> 2021.10.23일인데 2021-9-23 으로 결과값이 나오는 것을 발견하였음.
                이유는, toString 으로 하면 Month가 배열로 취급받아서 index 값으로 나오기 때문임.
             */

            val viewDate = "$month-$day"
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