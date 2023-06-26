package com.deslomator.tagtimer

import android.icu.text.SimpleDateFormat
import java.util.Locale


fun Long.toDateTime(): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.getDefault())
    return simpleDate.format(this)
}

fun Long.toElapsedTime(): String {
    val secs = (this / 1000).toInt()
    val hours = (secs / 3600)
    val minutes = (secs % 3600) / 60
    val seconds = (secs % 3600) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}