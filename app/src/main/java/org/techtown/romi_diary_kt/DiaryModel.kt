package org.techtown.romi_diary_kt

import java.io.Serializable

data class DiaryModel(
    var id: Int = 0, // 게시물 고유 id값
    var title: String = "",
    var content: String = "",
    var weatherType: Int = 0,
    var userDate: String = "",
    var writeDate: String = ""
) : Serializable
