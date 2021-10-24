package com.dongyang.android.pcheduler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
    }

    var startTimeCalendar = Calendar.getInstance()
    var endTimeCalendar = Calendar.getInstance()

    val currentYear = startTimeCalendar.get(Calendar.YEAR)
    val currentMonth = startTimeCalendar.get(Calendar.MONTH)
    val currentDate = startTimeCalendar.get(Calendar.DATE)
    

}