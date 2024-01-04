package org.techtown.romi_diary_kt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.techtown.romi_diary_kt.DatabaseHelper
import org.techtown.romi_diary_kt.R
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var mRvDiary: RecyclerView
    private lateinit var mAdapter: DiaryListAdapter
    private var mLstDiary: ArrayList<DiaryModel> = ArrayList()
    private lateinit var mDatabaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 데이터베이스 객체 초기화
        mDatabaseHelper = DatabaseHelper(this)

        mRvDiary = findViewById(R.id.rv_diary)
        mAdapter = DiaryListAdapter()

        mRvDiary.adapter = mAdapter

        val floatingActionButton = findViewById<FloatingActionButton>(R.id.btn_write)
        floatingActionButton.setOnClickListener {
            // 작성하기 버튼을 눌렀을 때 호출되는 곳
            val intent = Intent(this@MainActivity, DiaryDetailActivity::class.java)
            // 작성하기 화면으로 이동
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티의 재개

        // get load list
        setLoadRecentList()
    }

    private fun setLoadRecentList() {
        // 최근 데이터베이스 정보를 가져와서 리사이클러뷰에 갱신해준다.

        // 이전에 배열 리스트에 저장된 데이터가 있으면 비워버림.
        if (!mLstDiary.isEmpty()) {
            mLstDiary.clear()
        }

        mLstDiary = mDatabaseHelper.getDiaryListFromDB() // 데이터베이스로부터 저장되어 있는 DB를 확인하여 가져옴.
        mAdapter.setListInit(mLstDiary)
    }
}
