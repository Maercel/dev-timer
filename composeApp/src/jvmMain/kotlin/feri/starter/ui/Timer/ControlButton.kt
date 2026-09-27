package feri.starter.ui.Timer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import feri.starter.ui.theme.AppColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ControlButton(
    resId: DrawableResource,
    contentDescription: String?,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .alpha(if (enabled) 1f else 0.35f)
            .clip(CircleShape)
            .background(AppColors.Background)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(resId),
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
            tint = AppColors.Text
        )
    }
}
