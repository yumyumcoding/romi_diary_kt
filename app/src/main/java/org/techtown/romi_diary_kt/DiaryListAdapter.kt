package org.techtown.romi_diary_kt

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryListAdapter : RecyclerView.Adapter<DiaryListAdapter.ViewHolder>() {

    private var mLstDiary = ArrayList<DiaryModel>() // 다이어리 데이터들을 들고 있는 자료형
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val holder = LayoutInflater.from(mContext).inflate(R.layout.list_item_diary, parent, false)
        return ViewHolder(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weatherType = mLstDiary[position].weatherType

        when (weatherType) {
            0 -> holder.iv_weather.setImageResource(R.drawable.sun)
            1 -> holder.iv_weather.setImageResource(R.drawable.cloudy)
            2 -> holder.iv_weather.setImageResource(R.drawable.bad_cloud)
            3 -> holder.iv_weather.setImageResource(R.drawable.windy)
            4 -> holder.iv_weather.setImageResource(R.drawable.rain)
            5 -> holder.iv_weather.setImageResource(R.drawable.snow)
        }

        val title = mLstDiary[position].title
        val userDate = mLstDiary[position].userDate

        holder.tv_title.text = title
        holder.tv_user_date.text = userDate
    }

    override fun getItemCount(): Int {
        return mLstDiary.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_weather: ImageView = itemView.findViewById(R.id.iv_weather)
        var tv_title: TextView = itemView.findViewById(R.id.tv_title)
        var tv_user_date: TextView = itemView.findViewById(R.id.tv_user_date)

        init {
            itemView.setOnClickListener {
                val currentPosition = adapterPosition
                val diaryModel = mLstDiary[currentPosition]

                val diaryDetailIntent = Intent(mContext, DiaryDetailActivity::class.java)
                diaryDetailIntent.putExtra("diaryModel", diaryModel) // 다이어리 데이터 넘기기
                diaryDetailIntent.putExtra("mode", "detail") // 상세보기 모드로 설정
                mContext.startActivity(diaryDetailIntent)
            }

            itemView.setOnLongClickListener {
                val currentPosition = adapterPosition
                val diaryModel = mLstDiary[currentPosition]

                val strChoiceArray = arrayOf("수정 하기", "삭제 하기")
                AlertDialog.Builder(mContext)
                    .setTitle("원하시는 동작을 선택하세요")
                    .setItems(strChoiceArray) { dialogInterface: DialogInterface, position: Int ->
                        if (position == 0) {
                            val diaryDetailIntent = Intent(mContext, DiaryDetailActivity::class.java)
                            diaryDetailIntent.putExtra("diaryModel", diaryModel) // 다이어리 데이터 넘기기
                            diaryDetailIntent.putExtra("mode", "modify") // 수정 모드로 설정
                            mContext.startActivity(diaryDetailIntent)
                        } else {
                            mLstDiary.removeAt(currentPosition)
                            notifyItemRemoved(currentPosition)
                        }
                    }.show()
                false
            }
        }
    }

    fun setSampleList(lstDiary: ArrayList<DiaryModel>) {
        mLstDiary = lstDiary
    }
}
