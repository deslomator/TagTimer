package com.deslomator.tagtimer.util

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.LabelSort
import java.util.Locale


fun Long.toDateTime(): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.getDefault())
    return simpleDate.format(this)
}
fun Long.toDate(): String {
    val simpleDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return simpleDate.format(this)
}
fun Long.toTime(): String {
    val simpleDate = SimpleDateFormat("HH:mm", Locale.getDefault())
    return simpleDate.format(this)
}

fun Long.toElapsedTime(): String {
    val secs = (this / 1000).toInt()
    val hours = (secs / 3600)
    val minutes = (secs % 3600) / 60
    val seconds = (secs % 3600) % 60
    return String.format(
        Locale.getDefault(),
        "%02d:%02d:%02d",
        hours, minutes, seconds
    )
}

fun String?.getSort() = when (this) {
    LabelSort.NAME.sortId -> LabelSort.NAME
    else -> LabelSort.COLOR
}

fun List<EventForDisplay>.toCsv(session: Session, filtered: Boolean = false): String {
    val ev = this.map {
        "\"${it.person?.name}\",\"${it.place?.name}\",\"${it.tag?.name}\",\"${it.event.note}\",\"${it.event.elapsedTimeMillis}\""
    }.toMutableList()
    val headers = "\"person\",\"place\",\"tag\",\"note\",\"time\""
    val filterString = if (filtered) "filtered" else ""
    val dateString = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(session.sessionDateMillis)
    val metadata = "\"session: ${session.name}\",\"$filterString\",\"\",\"${session.notes}\",\"$dateString\""
    ev.add(0, metadata)
    ev.add(0, headers)
    return ev.joinToString("\n")
}

class EmptyDatabaseException : Exception("Nothing to process, empty Database")

fun String.toColor(): Color = Color(this.toLong(16))