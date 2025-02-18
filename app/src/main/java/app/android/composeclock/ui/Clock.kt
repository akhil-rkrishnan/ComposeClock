package app.android.composeclock.ui

import android.graphics.Paint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import app.android.composeclock.model.ClockHandStyle
import app.android.composeclock.model.FrameStyle
import app.android.composeclock.utils.FifteenMarkerRatio
import app.android.composeclock.utils.FiveMarkerRatio
import app.android.composeclock.utils.FrameInnerColor
import app.android.composeclock.utils.HourIntervalStepRatio
import app.android.composeclock.utils.MarkerStep
import app.android.composeclock.utils.NinetyMarkerRatio
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
    clockViewModel: ClockViewModel
) {
    val style = clockViewModel.style

    var centerOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    val hourHandDegrees by animateFloatAsState(
        targetValue = clockViewModel.hrHandDegrees, label = "Hour hand in degrees",
    )
    val minuteHandDegrees by animateFloatAsState(
        targetValue = clockViewModel.minHandDegrees, label = "Minute hand in degrees",
    )
    /*val secondHandDegrees by animateFloatAsState(
        targetValue = clockViewModel.secHandDegrees, label = "second hand in degrees",
        animationSpec = repeatable(
            iterations = 1,
            animation = tween(durationMillis = 500, delayMillis = 10, easing = LinearOutSlowInEasing),
        )
    )*/
    val secondHandDegrees = clockViewModel.secHandDegrees

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .clip(
            shape = RoundedCornerShape(10.dp)
        )
        .background(color = Color.White)
        .aspectRatio(1f), onDraw = {

        centerOffset = this.center

        val left = centerOffset.x - style.centerRadius
        val right = centerOffset.x + style.centerRadius
        val top = centerOffset.y - style.centerRadius
        val bottom = centerOffset.y + style.centerRadius

        if (style.frameStyle is FrameStyle.Round) {
            drawContext.canvas.nativeCanvas.apply {
                drawArc(
                    left,
                    top,
                    right,
                    bottom,
                    style.frameStyle.startAngle,
                    style.frameStyle.sweepAngle,
                    false,
                    Paint().apply {
                        strokeWidth = style.frameStyle.frameStrokeWidth
                        color = style.frameColor.toArgb()// style.frameColor.toArgb()
                        strokeMiter = Stroke.DefaultMiter
                        this.style = Paint.Style.STROKE
                    }
                )
                drawArc(
                    left - 10,
                    top - 10,
                    right + 10,
                    bottom + 10,
                    style.frameStyle.startAngle,
                    style.frameStyle.sweepAngle,
                    false,
                    Paint().apply {
                        strokeWidth = style.frameStyle.frameStrokeWidth
                        color = FrameInnerColor.toArgb()  //FrameInnerColor.toArgb()
                        strokeMiter = Stroke.DefaultMiter
                        this.style = Paint.Style.STROKE
                    }
                )
            }
            // markers for clock
            var textAngle = -90f
            var time = 1
            var timeText = ""
            for (angle in style.frameStyle.startAngle.toInt()..style.frameStyle.sweepAngle.toInt() step MarkerStep) {
                textAngle += angle
                val line = when {
                    angle % NinetyMarkerRatio == 0 -> {
                        style.ninetyStepColor to style.ninetyStepStroke
                    }

                    angle % FifteenMarkerRatio == 0 -> {
                        style.fifteenStepColor to style.fifteenStepStroke
                    }

                    angle % FiveMarkerRatio == 0 -> {
                        style.fiveStepColor to style.fiveStepStroke
                    }

                    else -> style.singleStepColor to style.singleStepStroke
                }
                timeText = when {
                    angle % HourIntervalStepRatio.toInt() == 0 && time <= 12 -> {
                        time.toString().apply { time++ }
                    }

                    else -> ""
                }
                val pointOne = markerLinePoints(
                    angle, style.centerRadius - style.frameStyle.markerStartDistanceFromArc,
                    centerOffset.x,
                    centerOffset.y
                )
                val pointTwo = markerLinePoints(
                    angle, style.centerRadius - style.frameStyle.markerEndDistanceFromArc,
                    centerOffset.x,
                    centerOffset.y
                )
                drawLine(
                    color = line.first,
                    start = pointOne,
                    end = pointTwo,
                    strokeWidth = line.second
                )
                if (angle % 30 == 0) {
                    drawContext.canvas.nativeCanvas.apply {
                        val textRadius =
                            style.centerRadius - style.frameStyle.markerEndDistanceFromArc - 38
                        val markPoint =
                            markerLinePoints(
                                -angle - 210,
                                textRadius,
                                centerOffset.x,
                                centerOffset.y
                            )
                        val x =
                            markPoint.x// abs(textRadius * cos(-angle.toFloat() + 180f) + centerOffset.x)
                        val y =
                            markPoint.y //abs(textRadius * sin(angle.toFloat() - 180f) + centerOffset.y)
                        drawText(
                            timeText,
                            x,
                            y,
                            Paint().apply {
                                textSize = 36f
                                textAlign = Paint.Align.CENTER
                            }
                        )
                    }
                }
            }


            val hourOffset = getClockHandPath(ClockHandStyle.HOUR, centerOffset)
            val minuteOffset = getClockHandPath(ClockHandStyle.MINUTE, centerOffset)
            val secondsOffset = getClockHandPath(ClockHandStyle.SECOND, centerOffset)

            val hourHand = Path().apply {
                moveTo(hourOffset.first.x, hourOffset.first.y)
                lineTo(hourOffset.second.x, hourOffset.second.y)
                lineTo(hourOffset.third.x, hourOffset.third.y)
                lineTo(hourOffset.first.x, hourOffset.first.y)
            }
            val minHand = Path().apply {
                moveTo(minuteOffset.first.x, minuteOffset.first.y)
                lineTo(minuteOffset.second.x, minuteOffset.second.y)
                lineTo(minuteOffset.third.x, minuteOffset.third.y)
                lineTo(minuteOffset.first.x, minuteOffset.first.y)
            }
            val secondHand = Path().apply {
                moveTo(secondsOffset.first.x, secondsOffset.first.y)
                lineTo(secondsOffset.second.x, secondsOffset.second.y)
                lineTo(secondsOffset.third.x, secondsOffset.third.y)
                lineTo(secondsOffset.first.x, secondsOffset.first.y)
            }
            rotate(hourHandDegrees, centerOffset) {
                drawPath(
                    path = hourHand,
                    color = style.hourHandColor,
                    style = Stroke(
                        width = 2f,
                        cap = StrokeCap.Round,
                        miter = 1f,
                        join = StrokeJoin.Round
                    )
                )
            }
            rotate(minuteHandDegrees, centerOffset) {
                drawPath(
                    path = minHand,
                    color = style.minHandColor
                )
            }
            rotate(secondHandDegrees, centerOffset) {
                drawPath(
                    path = secondHand,
                    color = style.secondHandColor
                )
            }
            // draw the hands
            drawCircle(color = style.centerLockColor, radius = 20f, centerOffset)

        }
    })
}

private fun getClockHandPath(clockHandStyle: ClockHandStyle, centerOffset: Offset) =
    when (clockHandStyle) {
        ClockHandStyle.HOUR -> {
            Triple(
                Offset( //middle
                    x = centerOffset.x,
                    y = centerOffset.y - 200f
                ), Offset( //bottomLeft
                    x = centerOffset.x - 15f,
                    y = centerOffset.y - 15f
                ), Offset( //bottomRight
                    x = centerOffset.x + 15f,
                    y = centerOffset.y - 15f
                )
            )
        }

        ClockHandStyle.MINUTE -> {
            Triple(
                Offset(  //middle
                    x = centerOffset.x,
                    y = centerOffset.y - 300f
                ), Offset( //bottomLeft
                    x = centerOffset.x - 15f,
                    y = centerOffset.y - 15f
                ), Offset( //bottomRight
                    x = centerOffset.x + 15f,
                    y = centerOffset.y - 15f
                )
            )
        }

        ClockHandStyle.SECOND -> {
            Triple(
                Offset(
                    x = centerOffset.x,
                    y = centerOffset.y - 250f
                ), Offset(
                    x = centerOffset.x - 15f,
                    y = centerOffset.y - 15f
                ), Offset(
                    x = centerOffset.x + 15f,
                    y = centerOffset.y - 15f
                )
            )
        }
    }

private fun markerLinePoints(
    thetaInDegrees: Int,
    radius: Float,
    cX: Float,
    cY: Float
): Offset {             //500   50                         210
    val x = abs(cX + (radius * sin(Math.toRadians(thetaInDegrees.toDouble()))).toFloat())
    val y = abs(cY + (radius * cos(Math.toRadians(thetaInDegrees.toDouble()))).toFloat())
    return Offset(x, y)
}