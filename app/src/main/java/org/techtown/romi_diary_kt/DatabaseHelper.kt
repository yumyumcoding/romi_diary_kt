package org.techtown.romi_diary_kt

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.techtown.romi_diary_kt.DiaryModel
import java.util.ArrayList

/*
* 데이터베이스 관리 유틸 클래스
*
* */
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "Mydiary.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Database Create
        // SQL이란 데이터베이스에 접근하기 위해서 명령을 내리는 쿼리문
        db.execSQL("CREATE TABLE IF NOT EXISTS DiaryInfo(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "content TEXT NOT NULL, " +
                "weatherType INTEGER NOT NULL, " +
                "userDate TEXT NOT NULL, " +
                "writeDate TEXT NOT NULL)") // 데이터를 삽입할 수 있는 테이블 생성 (최초 1회만 생성)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    /*
    * 다이어리 작성 데이터를 DB에 저장한다. (INSERT)
    * */

    fun setInsertDiaryList(_title: String, _content: String, _weatherType: Int, _userDate: String, _writeDate: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO DiaryInfo(title,content,weatherType,userDate,writeDate) VALUES('$_title','$_content','$_weatherType', '$_userDate', '$_writeDate')")
    }

    /*
    * 다이어리 작성 데이터를 조회하여 가지고 옴(SELECT)
    *
    * */

    fun getDiaryListFromDB(): ArrayList<DiaryModel> {
        val lstDiary = ArrayList<DiaryModel>()

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM DiaryInfo ORDER BY writeDate DESC", null)
        if (cursor.count != 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val content = cursor.getString(cursor.getColumnIndexOrThrow("content"))
                val weatherType = cursor.getInt(cursor.getColumnIndexOrThrow("weatherType"))
                val userDate = cursor.getString(cursor.getColumnIndexOrThrow("userDate"))
                val writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"))

                val diaryModel = DiaryModel()
                diaryModel.id = id
                diaryModel.title = title
                diaryModel.content = content
                diaryModel.weatherType = weatherType
                diaryModel.userDate = userDate
                diaryModel.writeDate = writeDate

                lstDiary.add(diaryModel)
            }
        }
        cursor.close()

        return lstDiary
    }

    /*
     * 기존 작성 데이터를 수정한다. (UPDATE)
     * */

    fun setUpdateDiaryList(_title: String, _content: String, _weatherType: Int, _userDate: String, _writeDate: String, _beforeDate: String) {
        val db = writableDatabase
        db.execSQL("UPDATE DiaryInfo SET title = '$_title',content = '$_content',weatherType = '$_weatherType',userDate = '$_userDate',writeDate = '$_writeDate' WHERE writeDate = '$_beforeDate'")

    }

    /*
     * 기존 작성 데이터를 삭제한다. (DELETE)
     * */

    fun setDeleteDiaryList(_writeDate: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM DiaryInfo WHERE writeDate = '$_writeDate'")
    }
}
