// 21.10.16 First Commit

package com.dongyang.android.pcheduler

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dongyang.android.pcheduler.database.CategoryEntity
import com.dongyang.android.pcheduler.database.ListDatabase
import com.dongyang.android.pcheduler.database.TaskEntity
import com.dongyang.android.pcheduler.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() {

    var categoryList = listOf<CategoryEntity>()
    var taskList = listOf<TaskEntity>()
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

        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.addItemDecoration(recyclerViewDecoration(20))

        getTask()

        binding.btnAdd.setOnClickListener{
            var text = binding.listText.text.toString() // 할 일 내용

            var task = TaskEntity(null,1,text,"","",null,20211101,"")
            insertTask(task)

            binding.listText.setText("")
        }



    }

    fun setDefaultCategory() {
        val DEFAULT_CATEGORY = CategoryEntity(1,"DEFAULT")
        insertCategory(DEFAULT_CATEGORY)
    }

    // RecyclerView 설정
    fun setRecyclerView(taskList : List<TaskEntity>) {
        binding.list.adapter = MainAdapter(this, taskList)
    }


    // TODO : AsyncTask -> Coroutine 변환 (10/24 김정호)

    /**
     * 안드로이드에서는 Lint를 통해 성능상 문제가 있을 수 있는 코드를 관리한다.
     * 밑의 코드에서는 AsyncTask 때문에 메모리 누수가 일어날 수 있는데, SuppressLint 를 통해 경고를 무시할 수 있다.
     */

    fun insertCategory(category : CategoryEntity) {

        /**
         * UI와 관련된 일들은 모두 MainThread 에서 실행한다.
         * 데이터 통신과 관련된 일은 WorkerThread(Background Thread) 에서 실행한다.
         */

        val getInsertCategory = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                // WorkerThread 에서 어떤 일을 할지 정의한다.
                db.listDAO().insertCategory(category)
            }


            override fun onPostExecute(result: Unit?) {
                // doInBackground 이후 어떤 일을 할 것인지 지정한다.
                super.onPostExecute(result)
                getCategory() // 데이터를 넣은 후 새로고침
            }

        }
        getInsertCategory.execute()
    }

    fun getCategory() {
        val getCategory = object : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                categoryList = db.listDAO().getCategory()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
            }
        }

        getCategory.execute()
    }

    fun deleteCategory() {

    }

    fun insertTask(task : TaskEntity) {

        val getInsertTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                // WorkerThread 에서 어떤 일을 할지 정의한다.
                db.listDAO().insertTask(task)
            }


            override fun onPostExecute(result: Unit?) {
                // doInBackground 이후 어떤 일을 할 것인지 지정한다.
                super.onPostExecute(result)
                getTask() // 데이터를 넣은 후 새로고침
            }

        }
        getInsertTask.execute()

    }

    fun getTask() {
        val getTask = object : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg p0: Unit?) {
                taskList = db.listDAO().getTask()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(taskList)
            }
        }

        getTask.execute()
    }


    // 리사이클러뷰 간격 조정
    inner class recyclerViewDecoration(private val height : Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = height
        }
    }




}