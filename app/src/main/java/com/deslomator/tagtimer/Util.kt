package com.deslomator.tagtimer

import android.icu.text.SimpleDateFormat
import java.util.Locale


fun Long.toDateTime(): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.getDefault())
    return simpleDate.format(this)
}