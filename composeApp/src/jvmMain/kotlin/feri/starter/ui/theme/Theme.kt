package feri.starter.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.Font
import starterdesktopapp.composeapp.generated.resources.Res
import starterdesktopapp.composeapp.generated.resources.montserrat
import starterdesktopapp.composeapp.generated.resources.saira_italic
import androidx.compose.material.MaterialTheme as M2Theme
import androidx.compose.material.Typography as M2Typography

object AppColors {
    val Background = Color.White
    val Surface = Color(0xFFE6E6E6)
    val SurfaceVariant = Color(0xFFD4D4D4)
    val Text = Color.Black
    val TextMuted = Color(0xFF5F5F5F)
}

object AppSpacing {
    val Screen = 10.dp
}

object AppShapes {
    val Card = RoundedCornerShape(5.dp)
    val Field = RoundedCornerShape(5.dp)
}

private val LightColors = lightColorScheme(
    primary = AppColors.Text,
    onPrimary = AppColors.Background,
    secondaryContainer = AppColors.SurfaceVariant,
    onSecondaryContainer = AppColors.Text,
    background = AppColors.Background,
    onBackground = AppColors.Text,
    surface = AppColors.Background,
    onSurface = AppColors.Text,
    surfaceVariant = AppColors.Surface,
    onSurfaceVariant = AppColors.TextMuted,
    surfaceContainer = AppColors.Surface,
    outline = AppColors.SurfaceVariant
)

@Composable
private fun montserratFont(weight: FontWeight) = Font(
    resource = Res.font.montserrat,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight))
)

@Composable
fun appFontFamily(): FontFamily = FontFamily(
    montserratFont(FontWeight.Light),
    montserratFont(FontWeight.Normal),
    montserratFont(FontWeight.Medium),
    montserratFont(FontWeight.SemiBold),
    montserratFont(FontWeight.Bold),
    montserratFont(FontWeight.Black)
)

@Composable
fun logoFontFamily(): FontFamily = FontFamily(
    Font(
        resource = Res.font.saira_italic,
        weight = FontWeight.Black,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(900),
            FontVariation.width(125f)
        )
    )
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val family = appFontFamily()
    val base = Typography()
    val typography = Typography(
        displayLarge = base.displayLarge.copy(fontFamily = family),
        displayMedium = base.displayMedium.copy(fontFamily = family),
        displaySmall = base.displaySmall.copy(fontFamily = family),
        headlineLarge = base.headlineLarge.copy(fontFamily = family),
        headlineMedium = base.headlineMedium.copy(fontFamily = family),
        headlineSmall = base.headlineSmall.copy(fontFamily = family),
        titleLarge = base.titleLarge.copy(fontFamily = family),
        titleMedium = base.titleMedium.copy(fontFamily = family),
        titleSmall = base.titleSmall.copy(fontFamily = family),
        bodyLarge = base.bodyLarge.copy(fontFamily = family),
        bodyMedium = base.bodyMedium.copy(fontFamily = family),
        bodySmall = base.bodySmall.copy(fontFamily = family),
        labelLarge = base.labelLarge.copy(fontFamily = family),
        labelMedium = base.labelMedium.copy(fontFamily = family),
        labelSmall = base.labelSmall.copy(fontFamily = family)
    )

    M2Theme(typography = M2Typography(defaultFontFamily = family)) {
        MaterialTheme(colorScheme = LightColors, typography = typography, content = content)
    }
}
