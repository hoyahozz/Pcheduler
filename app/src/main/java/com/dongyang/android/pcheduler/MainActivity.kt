// 21.10.16 First Commit

package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dongyang.android.pcheduler.Database.ListDatabase
import com.dongyang.android.pcheduler.Model.TaskEntity
import com.dongyang.android.pcheduler.UI.Fragment.CalFragment
import com.dongyang.android.pcheduler.UI.Fragment.ListFragment
import com.dongyang.android.pcheduler.ViewModel.ListViewModel
import com.dongyang.android.pcheduler.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    lateinit var db: ListDatabase // 데이터베이스
    private lateinit var binding: ActivityMainBinding // 뷰 바인딩
    private val viewModel: ListViewModel by viewModels()

    /*
         lateinit -> 전역변수 선언 후 null 값을 지정하지 않고 초기화
         var 키워드만 사용 가능하며, 기본 자료 타입(Int, Boolean, Double . .)에선 사용이 불가능하다.
         getter & setter 지정도 불가능함.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // 다크모드 비활성화
        // View Binding -> FindViewById 없이 View에서 바로 사용할 수 있게 해줌.
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = ListDatabase.getInstance(this)!! // NOT NULL

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.mainBnv.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_list ->
                        changeFragment(ListFragment())
                    R.id.menu_cal ->
                        changeFragment(CalFragment())
                }
                true
            }
            selectedItemId = R.id.menu_list
        }
    }

    private fun changeFragment(targetFragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, targetFragment)
            .commitAllowingStateLoss()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}