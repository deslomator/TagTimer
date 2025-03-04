package com.deslomator.tagtimer.util

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import com.deslomator.tagtimer.model.EventForDisplay
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.model.type.LabelSort
import kotlinx.coroutines.flow.Flow
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

inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow1,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(
        flow,
        flow2,
        flow3,
        flow4,
        flow5,
        flow6,
        flow7,
        flow8,
        flow9,
        flow10,
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
            args[6] as T7,
            args[7] as T8,
            args[8] as T9,
            args[9] as T10,
        )
    }
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