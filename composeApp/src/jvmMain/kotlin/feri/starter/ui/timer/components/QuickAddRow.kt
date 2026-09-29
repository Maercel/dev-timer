package feri.starter.ui.timer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.ui.theme.AppShapes

private val quickAddOptions = listOf(
    "+5m" to 5 * 60,
    "+10m" to 10 * 60,
    "+30m" to 30 * 60,
    "+1h" to 60 * 60
)

@Composable
internal fun QuickAddRow(
    enabled: Boolean,
    onAddTime: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.alpha(if (enabled) 1f else 0.35f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        quickAddOptions.forEach { (label, seconds) ->
            Box(
                modifier = Modifier
                    .shadow(5.dp, AppShapes.Card)
                    .clip(AppShapes.Card)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(enabled = enabled) { onAddTime(seconds) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
