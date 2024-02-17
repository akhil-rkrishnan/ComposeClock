package app.android.composeclock.ui

import androidx.compose.ui.graphics.Color
import app.android.composeclock.model.FrameStyle
import app.android.composeclock.model.Time
import app.android.composeclock.utils.CenterLockColor
import app.android.composeclock.utils.CenterRadius
import app.android.composeclock.utils.FifteenStepColor
import app.android.composeclock.utils.FifteenStepStroke
import app.android.composeclock.utils.FiveStepColor
import app.android.composeclock.utils.FiveStepStroke
import app.android.composeclock.utils.FrameOuterColor
import app.android.composeclock.utils.FrameStrokeWidth
import app.android.composeclock.utils.HourHandColor
import app.android.composeclock.utils.MarkerDistanceFromArc
import app.android.composeclock.utils.MinuteHandColor
import app.android.composeclock.utils.NinetyStepColor
import app.android.composeclock.utils.NinetyStepStroke
import app.android.composeclock.utils.SecondHandColor
import app.android.composeclock.utils.SingleStepColor
import app.android.composeclock.utils.SingleStepStroke
import app.android.composeclock.utils.StartAngle
import app.android.composeclock.utils.SweepAngle

data class ClockStyle(
    val centerRadius: Float = CenterRadius,
    val time: Time,
    val frameStyle: FrameStyle = FrameStyle.Round(
        startAngle = StartAngle,
        sweepAngle = SweepAngle,
        frameStrokeWidth = FrameStrokeWidth,
        markerStartDistanceFromArc = MarkerDistanceFromArc
    ),
    val frameColor: Color = FrameOuterColor,
    val minHandColor: Color = MinuteHandColor,
    val hourHandColor: Color = HourHandColor,
    val secondHandColor: Color = SecondHandColor,
    val ninetyStepColor: Color = NinetyStepColor,
    val fifteenStepColor: Color = FifteenStepColor,
    val fiveStepColor: Color = FiveStepColor,
    val singleStepColor: Color = SingleStepColor,
    val ninetyStepStroke: Float = NinetyStepStroke,
    val fifteenStepStroke: Float = FifteenStepStroke,
    val fiveStepStroke: Float = FiveStepStroke,
    val singleStepStroke: Float = SingleStepStroke,
    val centerLockColor: Color = CenterLockColor
)