package app.android.composeclock.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.composeclock.model.Time
import app.android.composeclock.utils.HourIntervalStepRatio
import app.android.composeclock.utils.MaxStepCounter
import app.android.composeclock.utils.StepRatio
import app.android.composeclock.utils.SweepAngle
import app.android.composeclock.utils.TimeInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ClockViewModel"

class ClockViewModel : ViewModel() {
    // give hour 0 for time 12.
    private var time by mutableStateOf(Time())
    var style by mutableStateOf(ClockStyle(time = time))
        private set
    var hrHandDegrees by mutableFloatStateOf(
        getHourAngle(time.hours)
    )
        private set
    var minHandDegrees by mutableFloatStateOf(
        getMinuteAngle(time.minutes)
    )
        private set

    var secHandDegrees by mutableFloatStateOf(
        getSecondsAngle(time.seconds)
    )
        private set
    private var minuteCounter by mutableIntStateOf(time.minutes)
    private var secondsCounter by mutableIntStateOf(time.seconds)

    private var resumeProcess by mutableStateOf(false)

    private fun startProcess() {
        viewModelScope.launch(Dispatchers.IO) {
            while (resumeProcess) {
                delay(TimeInterval) // delay 1 second
                if (secHandDegrees + StepRatio < SweepAngle) {
                    secHandDegrees += StepRatio
                } else {
                    secHandDegrees = StepRatio
                }
                secondsCounter++
                Log.d(TAG, "hrHand: $hrHandDegrees minHand: $minHandDegrees")
                if (secondsCounter == MaxStepCounter) {
                    minHandDegrees += StepRatio
                    minuteCounter++
                    secondsCounter = 0
                    if (minuteCounter == MaxStepCounter) {
                        minuteCounter = 0
                        hrHandDegrees += HourIntervalStepRatio // each digits is a multiple of 30f
                        val hour =
                            if (time.hours == 11) 0 // for time 12 we need to assign 0 to calculate proper angle
                            else time.hours + 1
                        time = time.copy(hours = hour) // increment the hour
                    }
                    hrHandDegrees =
                        (minuteCounter * 0.5f) + (time.hours * HourIntervalStepRatio) //angle position for hour hand based on the minute hand
                }
            }
        }
    }

    fun updateTime(hours: Int, minutes: Int, seconds: Int) {
        time = time.copy(hours = hours, minutes = minutes, seconds = seconds)
        hrHandDegrees = getHourAngle(time.hours)
        minHandDegrees = getHourAngle(time.minutes)
        secHandDegrees = getHourAngle(time.seconds)
        handleProcess(true)
        startProcess()
    }

    fun handleProcess(resume: Boolean) {
        resumeProcess = resume
    }

    private fun getHourAngle(hours: Int): Float {
        return if (hours in 1..11) {
            hours * HourIntervalStepRatio
        } else 0f
    }

    private fun getMinuteAngle(minutes: Int): Float {
        return minutes * StepRatio
    }

    private fun getSecondsAngle(seconds: Int): Float {
        return seconds * StepRatio
    }
}