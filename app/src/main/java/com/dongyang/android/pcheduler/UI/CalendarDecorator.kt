package com.dongyang.android.pcheduler.UI

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.*

class CalendarDecorator {
    class MinMaxDecorator(min: CalendarDay, max: CalendarDay) : DayViewDecorator {
        val maxDay = max
        val minDay = min

        override fun shouldDecorate(day: CalendarDay?): Boolean {

            return (day?.date!! < maxDay.date && day.date > minDay.date)
//            return (day?.year < maxDay.year && day.month < maxDay.month && day.day < maxDay.day)
//                    || (day?.year > minDay.year && day.month > minDay.month && day.day > minDay.day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#d2d2d2")) {})
            view?.setDaysDisabled(true)
        }
    }

    class SundayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK) // 어떤 요일인지 판별
            return (weekDay == Calendar.SUNDAY)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#FFF14E4E")) {})
        }
    }

    class SaturdayDecorator : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK) // 어떤 요일인지 판별
            return (weekDay == Calendar.SATURDAY)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.parseColor("#FF1812D5")) {})
        }
    }
}