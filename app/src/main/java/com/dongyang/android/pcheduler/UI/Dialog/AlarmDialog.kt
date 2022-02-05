package com.dongyang.android.pcheduler.UI.Dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.databinding.DialogDateandtimePickerBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-19
 * @Description : 알람 설정 다이얼로그
 */

class AlarmDialog(private val task: TaskEntity) : DialogFragment() {

    private lateinit var binding: DialogDateandtimePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDateandtimePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pickAlarm: String = ""
        val fm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Log.d("Alarm Dialog", "onViewCreated: ${task.alarm}")

        // Date And Time Picker 설정
        binding.dialogDateTimePicker.apply {
            this.setDisplayMonthNumbers(false)
            this.setDisplayYears(false)
            if(task.alarm != "") { // 기존 설정한 알람이 있다면 초기값으로 설정
                val defaultDate = fm.parse(task.alarm)
                this.setDefaultDate(defaultDate)
            }
            this.setIsAmPm(false)
            this.minDate = Date(System.currentTimeMillis())
            this.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.gowundodum))
            this.addOnDateChangedListener { displayed, date ->

                // D/Pick Date ::: Fri Nov 19 16:50:00 GMT+09:00 2021
                pickAlarm = fm.format(date)
                Log.d("Alarm Dialog", "Alarm Data :: $pickAlarm")
            }
        }

        // 확인 버튼 눌렀을 때
        binding.dialogDateTimePickerConfirmBtn.setOnClickListener {
            // FragmentResult
            parentFragmentManager.setFragmentResult(
                "alarmKey", bundleOf(
                    "alarmView" to pickAlarm,
                )
            )
            dismiss()
        }

        // 초기화 버튼 눌렀을 때
        binding.dialogDateTimePickerInitBtn.setOnClickListener {
            pickAlarm = ""
            parentFragmentManager.setFragmentResult(
                "alarmKey", bundleOf(
                    "alarmView" to pickAlarm
                )
            )
            dismiss()
        }

    }

}