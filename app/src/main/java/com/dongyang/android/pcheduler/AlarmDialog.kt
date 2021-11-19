package com.dongyang.android.pcheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.DialogDateandtimePickerBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-19
 * @Description : 알람 설정 다이얼로그
 */

class AlarmDialog(task : TaskEntity) : DialogFragment() {

    private lateinit var binding: DialogDateandtimePickerBinding
    private var task = task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDateandtimePickerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var pickAlarm : String = ""
        var calendar = Calendar.getInstance()
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        // Date And Time Picker 설정
        binding.dialogDateTimePicker.apply {
            this.setDisplayMonthNumbers(false)
            this.setDisplayYears(false)
            this.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.gowundodum))
            this.addOnDateChangedListener { displayed, date ->

                calendar.time = date

                // D/Pick Date ::: Fri Nov 19 16:50:00 GMT+09:00 2021
                val fm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                pickAlarm = fm.format(date)
                Log.d("Pick Date :: ", pickAlarm)
            }
        }

        // 확인 버튼 눌렀을 때
        binding.dialogDateTimePickerConfirmBtn.setOnClickListener {
            // FragmentResult
            parentFragmentManager.setFragmentResult(
                "alarmKey", bundleOf(
                    "alarm" to pickAlarm
                )
            )

            var pendingRequestCode : Int = task.id!!

            var pendingIntent = PendingIntent.getBroadcast(context, pendingRequestCode, intent, 0)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent
            )

            Toast.makeText(context, "Alarm Test :: $pendingRequestCode, ${calendar.timeInMillis}" , Toast.LENGTH_LONG).show()

            dismiss()
        }

        // 초기화 버튼 눌렀을 때
        binding.dialogDateTimePickerInitBtn.setOnClickListener {

            pickAlarm = ""
            parentFragmentManager.setFragmentResult(
                "alarmKey", bundleOf(
                    "alarm" to pickAlarm
                )
            )
            dismiss()
        }

    }

}