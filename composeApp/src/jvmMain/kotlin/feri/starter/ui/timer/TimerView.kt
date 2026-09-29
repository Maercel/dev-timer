package feri.starter.ui.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feri.starter.model.TimerState
import feri.starter.ui.TaskManagement
import feri.starter.ui.timer.components.CircularTimer
import feri.starter.ui.theme.AppColors

@Composable
fun TimerView(
    timerViewModel: TimerViewModel
) {
    val uiState by timerViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        TaskManagement(
            timerState = uiState.timerState,
            onStartClick = timerViewModel::start,
            onPauseClick = timerViewModel::pause,
            onStopClick = timerViewModel::stop,
            selectedDirectory = uiState.selectedDirectory,
            onDirectorySelected = timerViewModel::selectDirectory,
            Modifier.height(112.dp)
        )
        CircularTimer(
            timerSecondsLeft = uiState.timerSecondsLeft,
            timerSecondsTotal = uiState.timerSecondsTotal,
            isAdjustable = uiState.timerState == TimerState.IDLE,
            onDurationChange = timerViewModel::setDuration,
            onAddTime = timerViewModel::addTime,
            modifier = Modifier.weight(1F)
        )
    }
}
