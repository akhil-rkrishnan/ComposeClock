package app.android.composeclock.ui

import android.util.Range
import androidx.compose.ui.graphics.Color

data class ClockStyle(
    val centerRadius: Float = 200f,
    val time: Time,
    val frameStyle: FrameStyle = FrameStyle.Round(startAngle = 0f, sweepAngle = 360f, 5f, 20f),
    val frameColor: Color = Color.Black.copy(alpha = 0.4f),
    val minHandColor: Color = Color.Red,
    val hourHandColor: Color = Color.Blue,
    val secondHandColor: Color = Color.DarkGray,
    val ninetyStepColor: Color = Color.Red,
    val fifteenStepColor: Color = Color.Black,
    val fiveStepColor: Color = Color.Blue,
    val singleStepColor: Color = Color.LightGray,
    val ninetyStepStroke: Float = 8f,
    val fifteenStepStroke: Float = 6f,
    val fiveStepStroke: Float = 4f,
    val singleStepStroke: Float = 1f
)

data class Time(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

sealed class FrameStyle {
    data class Round(
        val startAngle: Float,
        val sweepAngle: Float,
        val frameStrokeWidth: Float,
        val markerStartDistanceFromArc: Float,
        val markerEndDistanceFromArc: Float = markerStartDistanceFromArc + 20f
    ) : FrameStyle()

    object Square : FrameStyle()
}

enum class ClockHandStyle {
    HOUR, MINUTE, SECOND
}