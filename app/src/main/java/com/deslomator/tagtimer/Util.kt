package com.deslomator.tagtimer

import android.icu.text.SimpleDateFormat
import java.util.Locale


fun Long.toDateTime(): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd  hh:mm", Locale.getDefault())
    return simpleDate.format(this)
}