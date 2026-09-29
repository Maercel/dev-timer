package feri.starter.ui.statistics.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.appFontFamily
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

data class BarSegment(val color: Color, val value: Double)

data class DayBar(val day: Int, val segments: List<BarSegment>) {
    val total: Double get() = segments.sumOf { it.value }
}

private const val GRID_LINES = 4

private fun niceStep(maxValue: Double): Double {
    if (maxValue <= 0.0) return 1.0
    val raw = maxValue / GRID_LINES
    val magnitude = 10.0.pow(floor(log10(raw)))
    return listOf(1.0, 2.0, 2.5, 5.0, 10.0).map { it * magnitude }.first { it >= raw }
}

private fun formatValue(value: Double): String =
    if (value >= 1000) "${(value / 1000).let { if (it % 1.0 == 0.0) it.toInt().toString() else "%.1f".format(it) }}k"
    else value.toInt().toString()

@Composable
fun DailyLinesChart(days: List<DayBar>, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val fontFamily = appFontFamily()
    val labelStyle = TextStyle(color = AppColors.TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Medium, fontFamily = fontFamily)
    val majorDayStyle = TextStyle(color = AppColors.Text, fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = fontFamily)
    val minorDayStyle = TextStyle(color = AppColors.TextMuted, fontSize = 7.sp, fontWeight = FontWeight.Medium, fontFamily = fontFamily)

    val progress = remember { Animatable(0f) }
    LaunchedEffect(days) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(durationMillis = 600, easing = FastOutSlowInEasing))
    }

    val step = niceStep(days.maxOfOrNull { it.total } ?: 0.0)
    val axisMax = step * GRID_LINES

    Canvas(modifier = modifier) {
        val yLabels = (0..GRID_LINES).map { formatValue(it * step) }
        val yLabelWidth = yLabels.maxOf { textMeasurer.measure(it, labelStyle).size.width }.toFloat()
        val xLabelHeight = textMeasurer.measure("30", majorDayStyle).size.height.toFloat()

        val left = yLabelWidth + 8.dp.toPx()
        val bottom = size.height - xLabelHeight - 6.dp.toPx()
        val top = xLabelHeight / 2f
        val plotWidth = size.width - left
        val plotHeight = bottom - top

        for (i in 0..GRID_LINES) {
            val y = bottom - plotHeight * i / GRID_LINES
            drawLine(
                color = if (i == 0) AppColors.TextMuted.copy(alpha = 0.5f) else Color.White,
                start = Offset(left, y),
                end = Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )
            val layout = textMeasurer.measure(yLabels[i], labelStyle)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(yLabelWidth - layout.size.width, y - layout.size.height / 2f)
            )
        }

        val slot = plotWidth / days.size
        val barWidth = min(slot * 0.62f, 14.dp.toPx())
        val radius = min(barWidth / 2f, 4.dp.toPx())

        days.forEachIndexed { index, bar ->
            val centerX = left + slot * index + slot / 2f
            var currentBottom = bottom
            val visibleSegments = bar.segments.filter { it.value > 0 }

            visibleSegments.forEachIndexed { segIndex, segment ->
                val height = (segment.value / axisMax * plotHeight * progress.value).toFloat()
                val rect = Rect(
                    offset = Offset(centerX - barWidth / 2f, currentBottom - height),
                    size = Size(barWidth, height)
                )
                drawSegment(rect, segment.color, if (segIndex == visibleSegments.lastIndex) radius else 0f)
                currentBottom -= height
            }

            val isMajor = bar.day == 1 || bar.day % 5 == 0
            val layout = textMeasurer.measure(bar.day.toString(), if (isMajor) majorDayStyle else minorDayStyle)
            drawText(
                textLayoutResult = layout,
                topLeft = Offset(
                    centerX - layout.size.width / 2f,
                    bottom + 6.dp.toPx() + (xLabelHeight - layout.size.height) / 2f
                )
            )
        }
    }
}

private fun DrawScope.drawSegment(rect: Rect, color: Color, topRadius: Float) {
    if (rect.height <= 0f) return
    val r = max(0f, min(topRadius, rect.height))
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = rect,
                topLeft = CornerRadius(r, r),
                topRight = CornerRadius(r, r),
                bottomRight = CornerRadius.Zero,
                bottomLeft = CornerRadius.Zero
            )
        )
    }
    drawPath(path, color)
}
