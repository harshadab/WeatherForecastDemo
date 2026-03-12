package com.assignment.core.utils

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

fun Int?.toHour(): String {
    if (this == null) return emptyString()
    val date = Date(this.toLong() * 1000)
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}

fun Int?.toDayOfWeek(): String {
    if (this == null) return emptyString()
    val date = Date(this.toLong() * 1000)
    val formatter = SimpleDateFormat("EEE", Locale.getDefault())
    return formatter.format(date)
}

fun String?.getFullUrl() = this?.let {
    if (this.startsWith("//")) {
        "https:$this"
    } else {
        this
    }
} ?: emptyString()

fun Double?.roundToString(): String {
    return this?.roundToInt()?.toString() ?: emptyString()
}

fun String?.orDefault(default: String = emptyString()) =
    if (this.isNullOrEmpty() || this.isBlank()) default else this

fun emptyString() = ""

fun formatHourLabel(hourString: String): String {
    val hour = hourString.substringBefore(":").toInt()
    val currentHour = LocalTime.now().hour

    if (hour == currentHour) return "Now"

    val amPmHour = when {
        hour == 0 -> "12AM"
        hour < 12 -> "${hour}AM"
        hour == 12 -> "12PM"
        else -> "${hour - 12}PM"
    }

    return amPmHour
}

fun isDayTime(): Boolean {
    val now = LocalTime.now()
    return now.isAfter(LocalTime.of(6, 0)) &&
            now.isBefore(LocalTime.of(18, 0))
}
