package app.android.composeclock.model

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