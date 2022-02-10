package com.dongyang.android.pcheduler.UI.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dongyang.android.pcheduler.Adapter.DateAdapter
import com.dongyang.android.pcheduler.Adapter.RecyclerViewDecoration
import com.dongyang.android.pcheduler.Adapter.TaskListAdapter
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.UI.CalendarDecorator
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.FragmentCalBinding
import com.dongyang.android.pcheduler.databinding.FragmentListBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-10-31
 * @Description : 달력 화면 출력 프래그먼트
 */

class CalFragment : Fragment() {

    private var _binding: FragmentCalBinding? = null // 뷰 바인딩
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()
    private val dateAdapter by lazy {
        DateAdapter(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalBinding.inflate(inflater, container, false)
        val view = binding.root

        val currentTime: Long = System.currentTimeMillis()
        val fm = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val today = fm.format(Date(currentTime))


        binding.dateRcv.apply {
            this.adapter = dateAdapter
            this.addItemDecoration(RecyclerViewDecoration(15))
            this.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.calView.apply {
            // 기본 UI 구성
            this.selectedDate = CalendarDay.from(Date(currentTime))
            this.addDecorator(CalendarDecorator.SaturdayDecorator())
            this.addDecorator(CalendarDecorator.SundayDecorator())
            val defaultMinimumDay = fm.parse("1920-01-01")
            val defaultMaximumDay = fm.parse("2100-01-01")
            this.state().edit().setMaximumDate(defaultMaximumDay).commit()
            this.state().edit().setMinimumDate(defaultMinimumDay).commit()

            viewModel.readDateData(today)
        }

        binding.calView.setOnDateChangedListener { widget, date, selected ->
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

            Log.d(TAG, "onCreateView: $pickDate")

            viewModel.readDateData(pickDate)
        }

        viewModel.currentData.observe(viewLifecycleOwner) {
            dateAdapter.submitList(it)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "CalFragment"
    }
}