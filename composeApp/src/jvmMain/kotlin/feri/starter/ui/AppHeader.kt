package feri.starter.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.logoFontFamily
import kotlin.math.PI
import kotlin.math.tan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppHeader(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { DevTimerLogo() },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = AppColors.Surface,
            titleContentColor = AppColors.Text
        )
    )
}

@Composable
private fun DevTimerLogo() {
    Column(
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .offset(y = (-3).dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = "DevTimer",
            color = AppColors.Text,
            maxLines = 1,
            style = TextStyle(
                fontSize = 28.sp,
                fontFamily = logoFontFamily(),
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                letterSpacing = (-0.5).sp,
                lineHeight = 1.em,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                )
            )
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .offset(y = (-6).dp)
                .height(4.dp)
        ) {
            val slant = size.height * tan(20.0 * PI / 180.0).toFloat()
            val path = Path().apply {
                moveTo(slant, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width - slant, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path, AppColors.Text)
        }
    }
}
