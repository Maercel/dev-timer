package feri.starter.ui.Timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.model.TimerState
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.AppShapes
import starterdesktopapp.composeapp.generated.resources.Res
import starterdesktopapp.composeapp.generated.resources.pause_button
import starterdesktopapp.composeapp.generated.resources.play_button
import starterdesktopapp.composeapp.generated.resources.stop_button

@Composable
internal fun ControlPanel(
    isDirectorySelected: Boolean,
    timerState: TimerState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isStartEnabled = isDirectorySelected && (timerState == TimerState.IDLE || timerState == TimerState.PAUSED)
    val isPauseEnabled = isDirectorySelected && (timerState == TimerState.RUNNING)
    val isStopEnabled = isDirectorySelected && (timerState == TimerState.RUNNING || timerState == TimerState.PAUSED)

    Card(
        modifier = modifier.fillMaxSize(),
        shape = AppShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface,
            contentColor = AppColors.Text
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Control Panel",
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.Text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlButton(
                    resId = Res.drawable.play_button,
                    contentDescription = "Play",
                    enabled = isStartEnabled,
                    onClick = onStartClick
                )
                ControlButton(
                    resId = Res.drawable.pause_button,
                    contentDescription = "Pause",
                    enabled = isPauseEnabled,
                    onClick = onPauseClick
                )
                ControlButton(
                    resId = Res.drawable.stop_button,
                    contentDescription = "Stop",
                    enabled = isStopEnabled,
                    onClick = onStopClick
                )
            }

            Text(
                if (isDirectorySelected) "Start, pause or stop the timer" else "Select a directory first",
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.TextMuted,
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
