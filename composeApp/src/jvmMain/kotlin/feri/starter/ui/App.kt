package feri.starter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import feri.starter.ui.timer.components.ControlPanel
import feri.starter.ui.timer.components.ProjectDirectory
import feri.starter.model.TimerState
import feri.starter.ui.timer.TimerView
import feri.starter.ui.timer.TimerViewModel
import feri.starter.ui.statistics.StatisticsUI
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.AppSpacing
import feri.starter.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    val viewModel = remember { TimerViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    var selectedTab by remember {
        mutableStateOf(AppTab.TIMER)
    }

    AppTheme {
        Scaffold(
            containerColor = AppColors.Background,
            topBar = { AppHeader() },
            bottomBar = {
                AppFooter(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    AppTab.TIMER -> TimerView(
                        viewModel
                    )
                    AppTab.STATISTICS -> StatisticsUI(viewModel.sessionStates)
                }
            }
        }
    }
}

@Composable
public fun TaskManagement(
    timerState: TimerState,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit,
    selectedDirectory: String?,
    onDirectorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDirectorySelected = !selectedDirectory.isNullOrEmpty()

    Row(
        modifier = modifier
            .padding(start = AppSpacing.Screen, end = AppSpacing.Screen, top = AppSpacing.Screen),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.Screen),
        verticalAlignment = Alignment.Top
    ) {
        ProjectDirectory(
            selectedDirectory,
            onDirectorySelected,
            Modifier.weight(1F)
        )
        ControlPanel(
            isDirectorySelected,
            timerState,
            onStartClick,
            onPauseClick,
            onStopClick,
            Modifier.weight(1F)
        )
    }
}
