package feri.starter

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import feri.starter.ui.App
import org.jetbrains.compose.resources.painterResource
import starterdesktopapp.composeapp.generated.resources.Res
import starterdesktopapp.composeapp.generated.resources.devtimer_logo_256
import java.awt.Dimension

fun main() = application {

    val windowState = rememberWindowState(
        width = 460.dp,
        height = 640.dp,
        position = WindowPosition(Alignment.Center)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "DevTimer",
        icon = painterResource(Res.drawable.devtimer_logo_256),
        state = windowState
    ) {
        window.minimumSize = Dimension(420, 580)
        App()
    }
}
