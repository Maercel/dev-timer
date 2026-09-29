package feri.starter.model

import feri.starter.ui.timer.INITIAL_TIME_SECONDS

enum class TimerState {
    IDLE, RUNNING, PAUSED
}

// has default state parameters
data class TimerUIState(
    val timerState: TimerState = TimerState.IDLE,
    val timerSecondsLeft: Int = INITIAL_TIME_SECONDS,
    val timerSecondsTotal: Int = INITIAL_TIME_SECONDS,
    val selectedDirectory: String? = null
)