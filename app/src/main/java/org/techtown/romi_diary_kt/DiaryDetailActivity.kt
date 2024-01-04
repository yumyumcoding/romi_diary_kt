package org.techtown.romi_diary_kt

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.radiobutton.MaterialRadioButton
import org.techtown.romi_diary_kt.DatabaseHelper
import org.techtown.romi_diary_kt.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DiaryDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mTvDate: TextView // 일시 설정 텍스트
    private lateinit var mEtTitle: EditText // 일기 제목
    private lateinit var mEtcontent: EditText // 일기 내용
    private lateinit var mRgWeather: RadioGroup

    private var mDetailMode = ""
    private var mBeforeDate = ""

    // intent로 받아온 게시글 기존 작성 일자
    private var mSelectedUserDate = "" // 선택된 일시 값

    private var mSelectWeatherType = -1

    private lateinit var mDatabaseHelper: DatabaseHelper // Database Util 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_detail)

        mDatabaseHelper = DatabaseHelper(this)

        mTvDate = findViewById(R.id.tv_date)
        mEtTitle = findViewById(R.id.et_title)
        mEtcontent = findViewById(R.id.et_content)
        mRgWeather = findViewById(R.id.rg_weather)

        val iv_back = findViewById<ImageView>(R.id.iv_back)
        val iv_check = findViewById<ImageView>(R.id.iv_check)

        mTvDate.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        iv_check.setOnClickListener(this) // 클릭 기능 부여

        // 기본으로 설정될 날짜의 값을 지정 (디바이스 현재 시간 기준)
        mSelectedUserDate = SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(Date())
        mTvDate.text = mSelectedUserDate

        // 이전 액티비티로부터 값을 전달받기
        val intent = intent
        if (intent.extras != null) {
            // intent putExtra 했던 데이터가 존재했다면 실행
            val diaryModel = intent.getSerializableExtra("diaryModel") as DiaryModel?
            // intent로 받아낸 게시글 모드
            mDetailMode = intent.getStringExtra("mode") ?: ""
            mBeforeDate = diaryModel?.writeDate ?: ""

            // 넘겨 받은 값을 활용해서 각 필드들에 설정해주기
            mEtTitle.setText(diaryModel?.title)
            mEtcontent.setText(diaryModel?.content)
            val weatherType = diaryModel?.weatherType ?: -1
            (mRgWeather.getChildAt(weatherType) as MaterialRadioButton).isChecked = true
            mTvDate.text = diaryModel?.userDate

            if (mDetailMode == "modify") {
                // 수정모드
                val tv_header_title = findViewById<TextView>(R.id.tv_header_title)
                tv_header_title.text = "일기 수정"
            } else if (mDetailMode == "detail") {
                // 상세보기 모드
                val tv_header_title = findViewById<TextView>(R.id.tv_header_title)
                tv_header_title.text = "일기 상세보기"

                // 읽기 전용 화면으로 표시
                mEtTitle.isEnabled = false
                mEtcontent.isEnabled = false
                mTvDate.isEnabled = false
                for (i in 0 until mRgWeather.childCount) {   // 라디오 그룹 내의 6개 버튼들을 반복하여 비활성화 처리 함
                    mRgWeather.getChildAt(i).isEnabled = false
                }
                // 작성 완료 버튼을 invisible(투명)처리 함.
                iv_check.visibility = View.INVISIBLE
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                // 뒤로가기
                finish()
            }
            R.id.iv_check -> {
                // 작성완료
                // 라디오 그룹의 버튼 클릭 현재 상황 가져오기
                // 선택된 날씨 값
                val mSelectedWeatherType = mRgWeather.indexOfChild(findViewById(mRgWeather.checkedRadioButtonId))
                // 입력 필드 작성란이 비어있는지 체크
                if (mEtTitle.text.length == 0 || mEtcontent.text.length == 0) {
                    Toast.makeText(this, "입력되지 않은 필드가 존재합니다.", Toast.LENGTH_SHORT).show()
                    return  // 밑의 로직 실행 x
                }

                // 날씨 선택이 되어있는지 체크
                if (mSelectedWeatherType == -1) {
                    // error
                    Toast.makeText(this, "날씨를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                ///////////// 에러 없으므로 데이터 저장
                var title = mEtTitle.text.toString()           // 제목 입력 값
                var content = mEtcontent.text.toString()       // 내용 입력 값
                var userDate = mSelectedUserDate                    // 사용자가 선택한 일시

                if (userDate == "") {
                    // 사용자가 별도 날짜 설정을 하지 않은 채로 작성 완료를 누른 경우
                    userDate = mTvDate.text.toString()
                }
                var writeDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREAN).format(Date()) // 작성완료 누른 시점의 시각

                // 액티비티의 현재 모드에 따라서 데이터 베이스에 저장 또는 업데이트
                if (mDetailMode == "modify") {
                    // 게시글 수정 모드
                    mDatabaseHelper.setUpdateDiaryList(title, content, mSelectedWeatherType, userDate, writeDate, mBeforeDate)
                    Toast.makeText(this, "다이어리 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 게시글 작성 모드
                    mDatabaseHelper.setInsertDiaryList(title, content, mSelectedWeatherType, userDate, writeDate)
                    Toast.makeText(this, "다이어리 등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                }
                finish() // 현재 액티비티 종료
            }
            R.id.tv_date -> {
                // 일시 설정 텍스트
                // 달력을 띄워서 사용자에게 일시를 입력 받는다.
                val calendar = Calendar.getInstance()
                val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    // 달력에 선택된 (년,월,일) 을 가지고 와서 다시 캘린더 함수에 넣어줘서 사용자가 선택한 요일을 알아낸다.
                    val innerCal = Calendar.getInstance()
                    innerCal.set(Calendar.YEAR, year)
                    innerCal.set(Calendar.MONTH, month)
                    innerCal.set(Calendar.DATE, dayOfMonth)

                    mSelectedUserDate = SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(innerCal.time)
                    mTvDate.text = mSelectedUserDate
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
                dialog.show() // 다이얼로그 활성화!
            }
        }
    }
}
