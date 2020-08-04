package com.andrius.notesappdemo.util

import java.text.SimpleDateFormat

object DateUtil {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}

enum class SortOrder {
    NONE, CONTENT, CREATION_DATE, MODIFICATION_DATE
}


