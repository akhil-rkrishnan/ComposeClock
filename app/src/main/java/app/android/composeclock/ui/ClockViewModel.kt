package app.android.composeclock.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ClockViewModel : ViewModel() {
    // give hour 0 for time 12.
    private var time by mutableStateOf(Time(0, 15, 0))
    var style by mutableStateOf(ClockStyle(time = time))
        private set
    var hrHandDegrees by mutableStateOf(
        if (time.hours != 12) {
            time.hours * 30f
        } else {
            0f
        }
    )
        private set
    var minHandDegrees by mutableStateOf(
        time.minutes * 6f
    )
        private set

    var secHandDegrees by mutableStateOf(
        time.seconds * 6f
    )
        private set
    var minuteCounter by mutableStateOf(time.minutes)
        private set
    var secondsCounter by mutableStateOf(time.seconds)
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                delay(1000) // delay 1 second
                secHandDegrees += 6
                secondsCounter++
                Log.d("Akhil", "hrHand: $hrHandDegrees minHand: $minHandDegrees")
                if (secondsCounter == 60) {
                    minHandDegrees += 6
                    minuteCounter++
                    secondsCounter = 0
                    if (minuteCounter == 60) {
                        minuteCounter = 0
                        hrHandDegrees += 30f // each digits is a multiple of 30f
                        val hour = if (time.hours == 11) 0 // for time 12 we need to assign 0 to calculate proper angle
                         else time.hours + 1
                        time = time.copy(hours = hour) // increment the hour
                    }
                    hrHandDegrees =
                        (minuteCounter * 0.5f) + (time.hours * 30) //angle position for hour hand based on the minute hand
                }
                if (secHandDegrees > 360) { //resetting angle to 0
                   // secHandDegrees = 0f
                }
            }
        }
    }

    fun updateTime(hours: Int, minutes: Int, seconds: Int) {
        time = time.copy(hours = hours, minutes = minutes, seconds = seconds)
    }
}