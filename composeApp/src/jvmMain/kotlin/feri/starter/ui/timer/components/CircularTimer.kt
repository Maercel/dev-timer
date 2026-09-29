package feri.starter.ui.timer.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.AppSpacing
import feri.starter.ui.theme.AppShapes
import feri.starter.ui.timer.MAX_TIME_SECONDS
import java.awt.Cursor
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

private const val SECONDS_PER_TURN = 7200f
private const val DEGREES_PER_MINUTE = 3f
private const val TICK_COUNT = 24
private const val DISK_FRACTION = 0.88f
private const val MAX_MINUTES = MAX_TIME_SECONDS / 60
private const val SCROLL_STEP_SECONDS = 5 * 60
private const val CENTER_DEAD_ZONE = 0.15f

private val endTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CircularTimer(
    timerSecondsLeft: Int,
    timerSecondsTotal: Int,
    isAdjustable: Boolean,
    onDurationChange: (Int) -> Unit,
    onAddTime: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val secondsLeft = max(0, timerSecondsLeft)
    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600 / 60).toString().padStart(2, '0')
    val seconds = (secondsLeft % 60).toString().padStart(2, '0')
    val timerText = if (hours > 0) "$hours:$minutes:$seconds" else "$minutes:$seconds"
    val endsAt = LocalTime.now().plusSeconds(secondsLeft.toLong()).format(endTimeFormatter)

    val currentTotal by rememberUpdatedState(timerSecondsTotal)
    val currentOnDurationChange by rememberUpdatedState(onDurationChange)

    val targetAngle = secondsLeft / SECONDS_PER_TURN * 360f
    val animatedAngle by animateFloatAsState(targetAngle, tween(durationMillis = 120))
    var dragAngle by remember { mutableStateOf<Float?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppSpacing.Screen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Screen)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .then(
                    if (isAdjustable) Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                    else Modifier
                )
                .onPointerEvent(PointerEventType.Scroll) { event ->
                    if (!isAdjustable) return@onPointerEvent
                    val scroll = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                    if (scroll == 0f) return@onPointerEvent
                    val next = if (scroll < 0f) {
                        (currentTotal / SCROLL_STEP_SECONDS + 1) * SCROLL_STEP_SECONDS
                    } else {
                        ((currentTotal - 1) / SCROLL_STEP_SECONDS) * SCROLL_STEP_SECONDS
                    }
                    currentOnDurationChange(next.coerceIn(60, MAX_TIME_SECONDS))
                }
                .pointerInput(isAdjustable) {
                    if (!isAdjustable) return@pointerInput

                    var lastAngle = 0f
                    var accumulated = 0f

                    fun isNearCenter(position: Offset): Boolean {
                        val dx = position.x - size.width / 2f
                        val dy = position.y - size.height / 2f
                        return sqrt(dx * dx + dy * dy) < size.width * CENTER_DEAD_ZONE
                    }

                    fun angleOf(position: Offset): Float {
                        val dx = position.x - size.width / 2f
                        val dy = position.y - size.height / 2f
                        return (atan2(dy, dx) * 180f / PI).toFloat()
                    }

                    detectDragGestures(
                        onDragStart = { start ->
                            lastAngle = angleOf(start)
                            accumulated = currentTotal / SECONDS_PER_TURN * 360f
                            dragAngle = accumulated
                        },
                        onDragEnd = { dragAngle = null },
                        onDragCancel = { dragAngle = null },
                        onDrag = { change, _ ->
                            change.consume()
                            val angle = angleOf(change.position)
                            if (isNearCenter(change.position)) {
                                lastAngle = angle
                                return@detectDragGestures
                            }
                            var delta = angle - lastAngle
                            if (delta > 180f) delta -= 360f
                            if (delta < -180f) delta += 360f
                            lastAngle = angle
                            accumulated = (accumulated + delta).coerceIn(DEGREES_PER_MINUTE, MAX_MINUTES * DEGREES_PER_MINUTE)
                            dragAngle = accumulated
                            val wheelMinutes = (accumulated / DEGREES_PER_MINUTE).roundToInt().coerceIn(1, MAX_MINUTES)
                            if (wheelMinutes * 60 != currentTotal) {
                                currentOnDurationChange(wheelMinutes * 60)
                            }
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            val displayAngle = dragAngle ?: animatedAngle
            val timerFontSize = (maxWidth.value * if (hours > 0) 0.12f else 0.16f).coerceAtMost(44f).sp
            val completedLap = displayAngle > 360.5f
            val lapSweep = if (displayAngle >= 0.5f && displayAngle % 360f < 0.5f) 360f else displayAngle % 360f

            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = minOf(size.width, size.height)
                val ringWidth = diameter * (1f - DISK_FRACTION) / 2f
                val arcDiameter = diameter - ringWidth
                val topLeft = Offset(
                    x = (size.width - arcDiameter) / 2f,
                    y = (size.height - arcDiameter) / 2f
                )

                drawCircle(
                    color = if (completedLap) AppColors.TextMuted else AppColors.Surface,
                    radius = arcDiameter / 2f,
                    style = Stroke(width = ringWidth)
                )

                drawArc(
                    color = AppColors.Text,
                    startAngle = -90f,
                    sweepAngle = lapSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = Size(arcDiameter, arcDiameter),
                    style = Stroke(width = ringWidth, cap = StrokeCap.Round)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(DISK_FRACTION)
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .background(AppColors.Background, CircleShape)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize(DISK_FRACTION)
                    .rotate(displayAngle)
            ) {
                val radius = size.minDimension / 2f
                val outer = radius * 0.88f
                val inner = radius * 0.76f
                val tickWidth = 2.dp.toPx()

                repeat(TICK_COUNT) { index ->
                    val theta = (index * 360f / TICK_COUNT - 90f) * PI.toFloat() / 180f
                    val direction = Offset(cos(theta), sin(theta))
                    drawLine(
                        color = AppColors.Text,
                        start = center + direction * inner,
                        end = center + direction * outer,
                        strokeWidth = tickWidth,
                        cap = StrokeCap.Round
                    )
                }
            }

            Text(
                text = timerText,
                color = AppColors.Text,
                fontSize = timerFontSize,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (timerFontSize.value * 0.6f).dp + 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, AppShapes.Card)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Ends: ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(endsAt)
                        }
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }

        QuickAddRow(
            enabled = isAdjustable,
            onAddTime = onAddTime
        )
    }
}
