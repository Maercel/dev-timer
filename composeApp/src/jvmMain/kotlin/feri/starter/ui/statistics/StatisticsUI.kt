package feri.starter.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.model.SessionState
import feri.starter.ui.statistics.components.BarSegment
import feri.starter.ui.statistics.components.DailyLinesChart
import feri.starter.ui.statistics.components.DayBar
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.AppSpacing
import feri.starter.ui.theme.AppShapes
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth

private val projectPalette = listOf(
    Color(0xFF111111),
    Color(0xFF8E8E8E),
    Color(0xFF55616B),
    Color(0xFFB3A99F),
    Color(0xFF3A3A3A)
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatisticsUI(sessionStates: List<SessionState>, modifier: Modifier = Modifier) {
    val currentMonth = LocalDate.now().month.name.lowercase().replaceFirstChar { it.uppercase() }
    val currentYear = LocalDate.now().year.toString()

    var expandedMonth by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    val months = listOf("January", "February", "March", "April",
        "May", "June", "July", "August", "September", "October",
        "November", "December")

    var expandedYear by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(currentYear) }
    val years = listOf("2024", "2025", "2026")

    val fieldColors = TextFieldDefaults.textFieldColors(
        textColor = AppColors.Text,
        backgroundColor = AppColors.Surface,
        trailingIconColor = AppColors.Text,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
    val fieldTextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)

    val filteredSessions = remember(sessionStates.toList(), selectedMonth, selectedYear) {
        sessionStates.filter { session ->
            session.date.year.toString() == selectedYear &&
                    session.date.month.name.lowercase().replaceFirstChar { it.uppercase() } == selectedMonth
        }
    }

    val projects = remember(filteredSessions) {
        filteredSessions.map { it.projectState.name }.distinct()
    }
    val projectColorMap = remember(projects) {
        projects.mapIndexed { index, name -> name to projectPalette[index % projectPalette.size] }.toMap()
    }

    val daysInMonth = remember(selectedMonth, selectedYear) {
        YearMonth.of(selectedYear.toInt(), Month.valueOf(selectedMonth.uppercase())).lengthOfMonth()
    }

    val days = remember(filteredSessions, daysInMonth) {
        (1..daysInMonth).map { day ->
            val segments = filteredSessions
                .filter { it.date.dayOfMonth == day }
                .groupBy { it.projectState.name }
                .map { (name, sessions) ->
                    BarSegment(
                        projectColorMap[name] ?: AppColors.TextMuted,
                        sessions.sumOf { it.linesAdded }.toDouble()
                    )
                }
            DayBar(day, segments)
        }
    }

    val linesAdded = filteredSessions.filter { it.linesAdded > 0 }.sumOf { it.linesAdded }
    val linesRemoved = filteredSessions.filter { it.linesAdded < 0 }.sumOf { -it.linesAdded }
    val sessionCount = filteredSessions.size
    val activeDays = filteredSessions.map { it.date.dayOfMonth }.distinct().size

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(AppSpacing.Screen),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Screen)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Screen)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedMonth,
                onExpandedChange = { expandedMonth = it },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = selectedMonth,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    textStyle = fieldTextStyle,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppShapes.Field,
                    colors = fieldColors
                )
                ExposedDropdownMenu(
                    expanded = expandedMonth,
                    onDismissRequest = { expandedMonth = false },
                    modifier = Modifier.background(AppColors.Background)
                ) {
                    months.forEach { month ->
                        DropdownMenuItem(
                            onClick = {
                                selectedMonth = month
                                expandedMonth = false
                            }
                        ) {
                            Text(
                                text = month,
                                color = AppColors.Text,
                                fontWeight = if (month == selectedMonth) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            ExposedDropdownMenuBox(
                expanded = expandedYear,
                onExpandedChange = { expandedYear = it },
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = selectedYear,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    textStyle = fieldTextStyle,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppShapes.Field,
                    colors = fieldColors
                )
                ExposedDropdownMenu(
                    expanded = expandedYear,
                    onDismissRequest = { expandedYear = false },
                    modifier = Modifier.background(AppColors.Background)
                ) {
                    years.forEach { year ->
                        DropdownMenuItem(
                            onClick = {
                                selectedYear = year
                                expandedYear = false
                            }
                        ) {
                            Text(
                                text = year,
                                color = AppColors.Text,
                                fontWeight = if (year == selectedYear) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Screen)
        ) {
            SummaryCard("Added", "+ %,d".format(linesAdded), Modifier.weight(1f))
            SummaryCard("Removed", "- %,d".format(linesRemoved), Modifier.weight(1f))
            SummaryCard("Sessions", "%,d".format(sessionCount), Modifier.weight(1f))
            SummaryCard("Active days", activeDays.toString(), Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(AppColors.Surface, AppShapes.Card)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Lines added per day",
                color = AppColors.Text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            if (projects.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    projects.take(5).forEach { name ->
                        LegendItem(name, projectColorMap[name] ?: AppColors.TextMuted)
                    }
                }

                DailyLinesChart(
                    days = days,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Za izbran mesec in leto še ni zabeleženih sej.",
                        color = AppColors.TextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(AppColors.Surface, AppShapes.Card)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = value,
            color = AppColors.Text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false
        )
        Text(
            text = label,
            color = AppColors.TextMuted,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun LegendItem(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = name,
            color = AppColors.Text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
