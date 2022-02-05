package com.dongyang.android.pcheduler.UI.Dialog

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.dongyang.android.pcheduler.AlarmReceiver
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.R
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.BottomsheetDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


/**
 * @Author : Jeong Ho Kim
 * @Created : 2021-11-09
 * @Description : 할 일을 눌렀을 때 나오는 바텀시트 클래스
 */

class DetailBottomSheet(private val task: TaskEntity, private val listViewModel: ListViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetDetailBinding
    private lateinit var db: ListDatabase
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
    ): View {
        binding = BottomsheetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.dbsEtContent.requestFocus() // EditText에 포커스를 올림
        // imm.showSoftInput(binding.dbsEtContent, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(view.windowToken, HIDE_NOT_ALWAYS)


        // 키보드 사라지면 다이얼로그 종료되게 설정
//        var keyboardVisibilityUtils = activity?.let {
//            KeyboardVisibilityUtils(it.window,
//                onHideKeyboard = {
//                    binding.root.run {
//                        dismiss()
//                    }
//                }
//            )
//        }

        db = ListDatabase.getInstance(requireContext())!! // DB

        var sTime = "" // 뷰에 올릴 값 ("MM-dd")
        var sDate = "" // 캘린더 다이얼로그로 넘길 날짜값 ("yyyy-MM-dd")
        var eTime = ""
        var alarmView = ""

        // FragmentResult ( Fragment <-> Fragment 통신) (Calendar)
        parentFragmentManager.setFragmentResultListener("requestKey", this) { resultKey, bundle ->
            val viewDate = bundle.getString("viewDate")
            pickDate = bundle.getString("pickDate")!!

            if (onButton == "start") {
                binding.dbsBtnStartDate.text = viewDate
                task.start_time = pickDate
                sDate = pickDate
            } else {
                binding.dbsBtnEndDate.text = viewDate
                task.end_time = pickDate
            }

            Log.d(TAG, "FragmentResult (Cal) :: ${viewDate.toString()}")
            Log.d(TAG, "FragmentResult (Cal) :: $pickDate")
        }

        // FragmentResult ( Fragment <-> Fragment 통신) (Alarm)
        parentFragmentManager.setFragmentResultListener("alarmKey", this) { resultKey, bundle ->
            alarmView = bundle.getString("alarmView")!!

            task.alarm = alarmView
            Log.d(TAG, "FragmentResult (Alarm) :: $alarmView")
        }

        // 시작 시간, 종료 시간이 널이 아니라면 버튼에 값 넣기
        task.start_time.let {
            sTime = task.start_time.substring(5, task.start_time.length) // 시작 버튼에 들어갈 값
            binding.dbsBtnStartDate.text = sTime
        }

        task.end_time.let {
            if (task.end_time != "") {
                eTime = it.substring(5, task.end_time.length)
                binding.dbsBtnEndDate.text = eTime
            }
        }

        binding.dbsEtContent.setText(task.content)
        binding.dbsEtContent.imeOptions = EditorInfo.IME_ACTION_DONE // EditText 완료 버튼 설정

        // 완료 버튼을 눌렀을 때 할 행동 설정
        binding.dbsEtContent.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // TODO : 완료 버튼을 눌렀을 때 데이터베이스 수정 일어나게 설정하기 (11/13) -> DONE
                task.content = binding.dbsEtContent.text.toString()

                listViewModel.updateTask(task)

                val calendar = Calendar.getInstance()
                val alarmManager =
                    requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(requireContext(), AlarmReceiver::class.java)
                intent.putExtra("content", task.content)
                intent.putExtra("id", task.id)

                if (alarmView != "") { // 알람을 설정했을 때만 알람매니저 설정

                    val fm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val alarmDate = fm.parse(alarmView)

                    calendar.time = alarmDate
                    val pendingIntent =
                        PendingIntent.getBroadcast(context, task.id!!, intent, PendingIntent.FLAG_IMMUTABLE)

                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent
                    )
                } else { // 알람 설정 안했을 때는 취소

                    val pendingIntent =
                        PendingIntent.getBroadcast(context, task.id!!, intent, PendingIntent.FLAG_IMMUTABLE)
                    task.alarm = ""
                    pendingIntent.cancel()
                }

                Log.d("Update Test :: ", task.start_time + " + " + task.end_time)
                Log.d("Update Test :: ", task.alarm)

                dismiss()
            }
            false
        }

        val manager = (context as AppCompatActivity).supportFragmentManager

        // 시작 버튼을 눌렀을 때 달력 출력되게 설정하였음.
        binding.dbsBtnStartDate.setOnClickListener {
            onButton = "start"
            val sCalendar = CalendarDialog(task, sDate, onButton) // 시작 날짜 전달
            sCalendar.show(parentFragmentManager, "CalendarView")
        }

        binding.dbsBtnEndDate.setOnClickListener {
            onButton = "end"
            val eCalendar = CalendarDialog(task, sDate, onButton)
            eCalendar.show(manager, "CalendarView")
        }

        binding.dbsBtnAlarm.setOnClickListener {
            val alarm = AlarmDialog(task)
            alarm.show(parentFragmentManager, alarm.tag)
        }
    }

    // 키보드가 올라오고 내려갈 때 동작하는 클래스
    class KeyboardVisibilityUtils(
        private val window: Window,
        //private val onShowKeyboard: ((keyboardHeight: Int) -> Unit)? = null,
        private val onShowKeyboard: (() -> Unit)? = null,
        private val onHideKeyboard: (() -> Unit)? = null
    ) {

        private val MIN_KEYBOARD_HEIGHT_PX = 150

        private val windowVisibleDisplayFrame = Rect()
        private var lastVisibleDecorViewHeight: Int = 0


        private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
            val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

            // Decide whether keyboard is visible from changing decor view height.
            if (lastVisibleDecorViewHeight != 0) {
                if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                    // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                    //val currentKeyboardHeight = window.decorView.height - windowVisibleDisplayFrame.bottom
                    // Notify listener about keyboard being shown.
                    //onShowKeyboard?.invoke(currentKeyboardHeight)
                    onShowKeyboard?.invoke()
                } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                    // Notify listener about keyboard being hidden.
                    onHideKeyboard?.invoke()
                }
            }
            // Save current decor view height for the next call.
            lastVisibleDecorViewHeight = visibleDecorViewHeight
        }

        init {
            window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }

        fun detachKeyboardListeners() {
            window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }
    
    companion object {
        private const val TAG = "DetailBottomSheet"
    }
}