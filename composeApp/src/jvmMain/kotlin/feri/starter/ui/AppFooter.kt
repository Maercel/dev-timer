package feri.starter.ui

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import starterdesktopapp.composeapp.generated.resources.Res
import starterdesktopapp.composeapp.generated.resources.bar_chart_icon
import starterdesktopapp.composeapp.generated.resources.timer_icon
import feri.starter.ui.theme.AppColors

enum class AppTab { TIMER, STATISTICS }

@Composable
internal fun AppFooter(
    modifier: Modifier = Modifier,
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = AppColors.Surface,
        contentColor = AppColors.Text
    ) {
        FooterItem(
            label = "Timer",
            icon = Res.drawable.timer_icon,
            selected = selectedTab == AppTab.TIMER,
            onClick = { onTabSelected(AppTab.TIMER) }
        )
        FooterItem(
            label = "Statistics",
            icon = Res.drawable.bar_chart_icon,
            selected = selectedTab == AppTab.STATISTICS,
            onClick = { onTabSelected(AppTab.STATISTICS) }
        )
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.FooterItem(
    label: String,
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(icon),
                contentDescription = "$label Icon",
                modifier = Modifier.size(20.dp)
            )
        },
        label = {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppColors.Text,
            selectedTextColor = AppColors.Text,
            indicatorColor = AppColors.Background,
            unselectedIconColor = AppColors.TextMuted,
            unselectedTextColor = AppColors.TextMuted
        )
    )
}
