package com.andrius.notesappdemo.util

import java.util.*

fun String.toDate(): Date {
    return DateUtil.dateFormat.parse(this)
}

fun Date.toFormattedString(): String {
    return DateUtil.dateFormat.format(this)
}