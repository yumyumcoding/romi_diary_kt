package org.techtown.romi_diary_kt

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.techtown.romi_diary_kt.DatabaseHelper
import org.techtown.romi_diary_kt.R

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.ViewHolder>() {

    private var mLstDiary: ArrayList<DiaryModel>? = null // 다이어리 데이터를 들고있는 자료형
    private var mContext: Context? = null
    private var mDatabaseHelper: DatabaseHelper? = null // 데이터베이스 헬퍼 클래스

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        mDatabaseHelper = DatabaseHelper(mContext)
        val holder = LayoutInflater.from(mContext).inflate(R.layout.list_item_diary, parent, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherType = mLstDiary?.get(position)?.weatherType ?: -1

        when (weatherType) {
            0 -> holder.iv_weather.setImageResource(R.drawable.sun)
            1 -> holder.iv_weather.setImageResource(R.drawable.cloudy)
            2 -> holder.iv_weather.setImageResource(R.drawable.bad_cloud)
            3 -> holder.iv_weather.setImageResource(R.drawable.windy)
            4 -> holder.iv_weather.setImageResource(R.drawable.rain)
            5 -> holder.iv_weather.setImageResource(R.drawable.snow)
        }

        val title = mLstDiary?.get(position)?.title ?: ""
        val userDate = mLstDiary?.get(position)?.userDate ?: ""

        holder.tv_title.text = title
        holder.tv_user_date.text = userDate
    }

    override fun getItemCount(): Int {
        return mLstDiary?.size ?: 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_weather: ImageView = itemView.findViewById(R.id.iv_weather)
        val tv_title: TextView = itemView.findViewById(R.id.tv_title)
        val tv_user_date: TextView = itemView.findViewById(R.id.tv_user_date)

        init {
            itemView.setOnClickListener { view ->
                val currentPosition = adapterPosition
                val diaryModel = mLstDiary?.get(currentPosition)

                val diaryDetailIntent = Intent(mContext, DiaryDetailActivity::class.java)
                diaryDetailIntent.putExtra("diaryModel", diaryModel) // 다이어리 데이터 넘기기
                diaryDetailIntent.putExtra("mode", "detail") // 상세보기 모드로 설정
                mContext?.startActivity(diaryDetailIntent)
            }

            itemView.setOnLongClickListener { view ->
                val currentPosition = adapterPosition
                val diaryModel = mLstDiary?.get(currentPosition)

                val strChoiceArray = arrayOf("수정 하기", "삭제 하기")
                // 팝업 화면 표시
                AlertDialog.Builder(mContext)
                    .setTitle("원하시는 동작을 선택하세요")
                    .setItems(strChoiceArray) { dialogInterface, position ->
                        if (position == 0) {
                            // 수정하기 버튼을 눌렀을 때

                            // 화면 이동 및 다이어리 데이터 다음
                            val diaryDetailIntent = Intent(mContext, DiaryDetailActivity::class.java)
                            diaryDetailIntent.putExtra("diaryModel", diaryModel) // 다이어리 데이터 넘기기
                            diaryDetailIntent.putExtra("mode", "modify") // 수정 모드로 설정
                            mContext?.startActivity(diaryDetailIntent)
                        } else {
                            // 삭제하기 버튼을 눌렀을 때
                            // delete database data
                            diaryModel?.writeDate?.let { mDatabaseHelper?.setDeleteDiaryList(it) }
                            // delete UI
                            mLstDiary?.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                        }
                    }.show()
                false
            }
        }
    }

    fun setListInit(lstDiary: ArrayList<DiaryModel>) {
        mLstDiary = lstDiary
        notifyDataSetChanged() // 리사이클러뷰 새로고침
    }
}
