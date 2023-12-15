package org.techtown.romi_diary_kt

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DiaryDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mTvDate: TextView // 일시 설정 텍스트
    private lateinit var mEtTitle: EditText // 일기 제목
    private lateinit var mEtContent: EditText // 일기 내용
    private lateinit var mRgWeather: RadioGroup

    private var mDetailMode = "" // intent로 받아낸 게시글 모드
    private var mBeforeDate = "" // intent로 받아낸 게시글 기존 작성 일자
    private var mSelectedUserDate = "" // 선택된 일시 값
    private var mSelectedWeatherType = -1 // 선택된 날씨 값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_detail)

        mTvDate = findViewById(R.id.tv_date)
        mEtTitle = findViewById(R.id.et_title)
        mEtContent = findViewById(R.id.et_content)
        mRgWeather = findViewById(R.id.rg_weather)

        val ivBack = findViewById<ImageView>(R.id.iv_back)
        val ivCheck = findViewById<ImageView>(R.id.iv_check)

        mTvDate.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivCheck.setOnClickListener(this)

        // 기본으로 설정될 날짜의 값을 지정 (디바이스 현재 시간 기준)
        mSelectedUserDate = SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(Date())
        mTvDate.text = mSelectedUserDate
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> finish() // 뒤로가기
            R.id.iv_check -> {
                // 작성완료
                mSelectedWeatherType = mRgWeather.indexOfChild(findViewById(mRgWeather.checkedRadioButtonId))

                // 입력 필드 작성란이 비어있는지 체크
                if (mEtTitle.text.isEmpty() || mEtContent.text.isEmpty()) {
                    Toast.makeText(this, "입력되지 않은 필드가 존재합니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                // 날씨 선택이 되어있는지 체크
                if (mSelectedWeatherType == -1) {
                    Toast.makeText(this, "날씨를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return
                }

                // 에러 없으므로 데이터 저장
                val title = mEtTitle.text.toString() // 제목 입력 값
                val content = mEtContent.text.toString() // 내용 입력 값
                val userDate = mSelectedUserDate // 사용자가 선택한 일시

                val writeDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREAN).format(Date())
                finish() // 현재 액티비티 종료
            }
            R.id.tv_date -> {
                // 일시 설정 텍스트
                val calendar = Calendar.getInstance()
                val dialog = DatePickerDialog(
                    this,
                    { _, year, month, dayOfMonth ->
                        val innerCal = Calendar.getInstance()
                        innerCal.set(Calendar.YEAR, year)
                        innerCal.set(Calendar.MONTH, month)
                        innerCal.set(Calendar.DATE, dayOfMonth)

                        mSelectedUserDate =
                            SimpleDateFormat("yyyy/MM/dd E요일", Locale.KOREAN).format(innerCal.time)
                        mTvDate.text = mSelectedUserDate
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
                )
                dialog.show() // 다이얼로그 활성화!
            }
        }
    }
}
