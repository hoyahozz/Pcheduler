// 21.10.16 First Commit

package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.IllegalArgumentException

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    lateinit var db : ListDatabase // 데이터베이스
    private lateinit var binding : ActivityMainBinding // 뷰 바인딩
    /*
         lateinit -> 전역변수 선언 후 null 값을 지정하지 않고 초기화
         var 키워드만 사용 가능하며, 기본 자료 타입(Int, Boolean, Double . .)에선 사용이 불가능하다.
         getter & setter 지정도 불가능함.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                when(item.itemId) {
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

    private fun changeFragment(targetFragment : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, targetFragment)
            .commitAllowingStateLoss()
    }
}