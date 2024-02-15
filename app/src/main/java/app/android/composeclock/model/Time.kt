package app.android.composeclock.model

data class Time(
    val hours: Int = 0, // 0 represents time 12 in the clock
    val minutes: Int = 0,
    val seconds: Int = 0,
) {
    override fun toString(): String {
        var formattedString = ""
        formattedString += if (hours < 10) "0$hours " else "$hours "
        formattedString += if (minutes < 10) ": 0$minutes " else ": $minutes "
        formattedString += if (seconds < 10) ": 0$seconds" else ": $seconds"
        return formattedString
    }
}