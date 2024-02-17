package app.android.composeclock.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import app.android.composeclock.utils.SecondHandColor

data class Time(
    val hours: Int = 0, // 0 represents time 12 in the clock
    val minutes: Int = 0,
    val seconds: Int = 0,
) {
    override fun toString(): String {
        var formattedString = ""
        formattedString += if (hours == 0) "12" else if (hours < 10) "0$hours" else "$hours"
        formattedString += if (minutes < 10) ":0$minutes" else ":$minutes"
        formattedString += if (seconds < 10) ":0$seconds" else ":$seconds"
        return formattedString
    }
    fun asAnnotatedString(): AnnotatedString {
        return buildAnnotatedString {
            append(if (hours == 0) "12" else if (hours < 10) "0$hours" else "$hours")
            append(if (minutes < 10) ":0$minutes" else ":$minutes")
            withStyle(style = SpanStyle(color = SecondHandColor)) {
                append(if (seconds < 10) ":0$seconds" else ":$seconds")
            }
        }
    }
}