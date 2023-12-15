package org.techtown.romi_diary_kt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mRvDiary: RecyclerView
    private lateinit var mAdapter: DiaryListAdapter
    private lateinit var mLstDiary: ArrayList<DiaryModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLstDiary = ArrayList()

        mRvDiary = findViewById(R.id.rv_diary)

        mAdapter = DiaryListAdapter()

        // 다이어리 샘플 아이템 1개 생성
        val item = DiaryModel().apply {
            id = 0
            title = "다이어리 어플 만들기 도전!"
            content = "1일차"
            userDate = "2023/12/17/SAT"
            writeDate = "2023/12/17/SAT"
            weatherType = 0
        }
        mLstDiary.add(item)

        val item2 = DiaryModel().apply {
            id = 0
            title = "다이어리 어플 만들기 도전!"
            content = "2일차"
            userDate = "2023/12/17/SAT"
            writeDate = "2023/12/17/SAT"
            weatherType = 1
        }
        mLstDiary.add(item2)

        val item3 = DiaryModel().apply {
            id = 0
            title = "다이어리 어플 만들기 도전!"
            content = "3일차"
            userDate = "2023/12/17/SAT"
            writeDate = "2023/12/17/SAT"
            weatherType = 2
        }
        mLstDiary.add(item3)

        mAdapter.setSampleList(mLstDiary)
        mRvDiary.adapter = mAdapter

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.btn_write)
        floatingActionButton.setOnClickListener {
            // 작성하기 버튼을 눌렀을 때 호출되는 곳
            val intent = Intent(this@MainActivity, DiaryDetailActivity::class.java)
            // 작성하기 화면으로 이동
            startActivity(intent)
        }
    }
}
