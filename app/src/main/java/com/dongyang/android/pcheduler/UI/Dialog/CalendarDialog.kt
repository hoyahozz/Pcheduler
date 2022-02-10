package com.dongyang.android.pcheduler.UI.Dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.UI.CalendarDecorator
import com.dongyang.android.pcheduler.databinding.DialogCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-19
 * @Description : 캘린더 날짜 설정 다이얼로그
 */

// TODO : 종료 날짜가 시작 날짜보다 멀어야 함.

class CalendarDialog(
    private val task: TaskEntity,
    private val sDate: String, // 시작 날짜
    private val eDate: String, // 종료 날짜
    private val type: String // 어떤 버튼으로 다이얼로그를 호출했는지
) : DialogFragment() {
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
        Log.d("CalendarDialog", "sDate : $sDate")
        Log.d("CalendarDialog", "eDate : $eDate")

        dialogBinding.dialogDatePicker.apply {
            // 토요일, 일요일 데코레이터 추가
            this.addDecorator(CalendarDecorator.SundayDecorator())
            this.addDecorator(CalendarDecorator.SaturdayDecorator())

            val defaultMinimumDay = fm.parse("1920-01-01")
            val defaultMaximumDay = fm.parse("2100-01-01")
            this.state().edit().setMaximumDate(defaultMaximumDay).commit()
            this.state().edit().setMinimumDate(defaultMinimumDay).commit()
            // 시작 날짜가 있으면 시작 날짜보다 늦은 날만 설정할 수 있게
            // 없으면 현재 날짜보다 늦은 날만 설정할 수 있게


            // 달력 시작 날짜 설정
            when (type) {
                "start" -> {
                    if (sDate != "") { // 사용자가 설정한 시작 날짜가 있으면
                        this.selectedDate = CalendarDay.from(fm.parse(sDate)) // 현재 날짜를 지정 날짜로
                    } else { // 만약 없다면 DB에 있는 시간으로 사용
                        this.selectedDate = CalendarDay.from(fm.parse(task.start_time)) // 현재 날짜를 지정 날짜로
                    }

                    if (eDate != "") { // 사용자가 설정한 종료 날짜가 있으면
                        this.addDecorator(CalendarDecorator.MinMaxDecorator
                            (CalendarDay.from(fm.parse(eDate)), CalendarDay.from(defaultMaximumDay)))
                    } else {
                        if (task.end_time != "") {
                            this.addDecorator(CalendarDecorator.MinMaxDecorator
                                (CalendarDay.from(fm.parse(task.end_time)), CalendarDay.from(defaultMaximumDay)))
                        }
                    }
                }

                "end" -> {
                    if (eDate != "") { // 사용자가 설정한 시작 날짜가 있으면
                        this.selectedDate = CalendarDay.from(fm.parse(eDate)) // 현재 날짜를 지정 날짜로
                    } else { // 만약 없다면 DB에 있는 시간으로 사용
                        if (task.end_time != "") {
                            this.selectedDate = CalendarDay.from(fm.parse(task.end_time)) // 현재 날짜를 지정 날짜로
                        }
                    }

                    if (sDate != "") { // 다이얼로그에서 사용자가 설정한 시작 날짜가 있으면
                        Log.d("sDate", "onViewCreated: $sDate")
                        this.addDecorator(
                            CalendarDecorator.MinMaxDecorator(
                                CalendarDay.from(
                                    defaultMinimumDay
                                ), CalendarDay.from(fm.parse(sDate))
                            )
                        )
                    } else { // 다이얼로그에서 사용자가 설정한 시작 날짜가 없으면
                        Log.d("sDate", "onViewCreated: ${task.start_time}")
                        this.addDecorator(CalendarDecorator.MinMaxDecorator
                            (CalendarDay.from(defaultMinimumDay), CalendarDay.from(fm.parse(task.start_time))))

                        this.setCurrentDate(fm.parse(task.start_time))
                    }


                }


            }

            // 날짜를 선택했을 때 나타나는 이벤트 리스너
            // date -> CalendarDay(2021-10-23)
            this.setOnDateChangedListener { widget, date, selected ->
                // 0이 붙게 convert 한다.
                val month =
                    if (date.month + 1 < 10) {
                        "0" + (date.month + 1).toString()
                    } else {
                        (date.month + 1).toString()
                    }
                val day =
                    if (date.day < 10) {
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
}