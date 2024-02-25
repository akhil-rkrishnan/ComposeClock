package app.android.composeclock.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.android.composeclock.model.Time
import app.android.composeclock.utils.HourIntervalStepRatio
import app.android.composeclock.utils.MaxStepCounter
import app.android.composeclock.utils.StepRatio
import app.android.composeclock.utils.SweepAngle
import app.android.composeclock.utils.TimeInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "ClockViewModel"

class ClockViewModel : ViewModel() {
    // give hour 0 for time 12.
    private var time by mutableStateOf(Time())
    var style by mutableStateOf(ClockStyle(time = time))
        private set
    private var minuteCounter by mutableIntStateOf(time.minutes)
    private var secondsCounter by mutableIntStateOf(time.seconds)
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

    private val _randomTimes = mutableStateListOf<Time>()
    val randomTimes get() = _randomTimes

    private var resumeProcess = false
    private var timeJob: Job? = null
    private var updateTime: Job? = null

    var digitalTime by mutableStateOf(time.asAnnotatedString())
        private set

    init {
        refreshTimes()
    }

    private fun startProcess() {
        timeJob = viewModelScope.launch(Dispatchers.Main) {
            while (resumeProcess) {
                delay(TimeInterval) // delay 1 second
                if (secHandDegrees + StepRatio < SweepAngle) {
                    secHandDegrees += StepRatio
                } else {
                    secHandDegrees = 0f
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
                        time = time.copy(hours = hour, minutes = 0) // increment the hour
                    }
                    hrHandDegrees =
                        (minuteCounter * 0.5f) + (time.hours * HourIntervalStepRatio) //angle position for hour hand based on the minute hand
                }
                setDisplayTime(Time(time.hours, minuteCounter, secondsCounter))
            }
        }
    }

    fun updateTime(newTime: Time) {
        updateTime?.cancel()
        updateTime = viewModelScope.launch {
            handleProcess(false)
            time = newTime
            delay(150)
            minuteCounter = time.minutes
            secondsCounter = time.seconds
            hrHandDegrees = getHourAngle(time.hours)
            minHandDegrees = getMinuteAngle(time.minutes)
            secHandDegrees = getSecondsAngle(time.seconds)
            digitalTime = AnnotatedString("")
            handleProcess(true)
            startProcess()
        }

    }

    fun refreshTimes(limit: Int = 5) {
        _randomTimes.clear()
        _randomTimes.addAll(getRandomTimes(limit))
    }

    private fun setDisplayTime(time: Time) {
        digitalTime = time.asAnnotatedString()
    }

    fun handleProcess(resume: Boolean) {
        resumeProcess = resume
        if (!resume) {
            timeJob?.cancel()
        }
    }

    private fun getHourAngle(hours: Int): Float {
        return if (hours in 1..11) {
            (minuteCounter * 0.5f) + (time.hours * HourIntervalStepRatio)
        } else (minuteCounter * 0.5f) + 0f
    }

    private fun getMinuteAngle(minutes: Int): Float {
        return minutes * StepRatio
    }

    private fun getSecondsAngle(seconds: Int): Float {
        return seconds * StepRatio
    }

    private fun getRandomTimes(limit: Int = 5): List<Time> {
        val timeList = arrayListOf<Time>()
        for (i in 0 until limit) {
            val hour = (0..11).random()
            val minutes = (0..45).random()
            val seconds = (0..59).random()
            timeList.add(Time(hour, minutes, seconds))
        }
        return timeList
    }
}