package feri.starter.ui.timer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feri.starter.ui.theme.AppColors
import feri.starter.ui.theme.AppShapes
import java.io.File
import javax.swing.JFileChooser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.swing.filechooser.FileSystemView

private val directoryChooser by lazy {
    JFileChooser(FileSystemView.getFileSystemView()).apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        putClientProperty("FileChooser.useShellFolder", false)
    }
}

@Composable
internal fun ProjectDirectory(
    selectedDirectory: String?,
    onDirectorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) { directoryChooser }
    }

    fun openFileDialog(): Unit {
        val state = directoryChooser.showOpenDialog(null)

        if (state == JFileChooser.APPROVE_OPTION) {
            onDirectorySelected(directoryChooser.selectedFile.absolutePath)
        }
    }

    Card(
        onClick = { openFileDialog() },
        modifier = modifier.fillMaxSize(),
        shape = AppShapes.Card,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Surface,
            contentColor = AppColors.Text
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Project Directory",
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.Text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            val hasDirectory = !selectedDirectory.isNullOrEmpty()

            Text(
                text = if (hasDirectory) File(selectedDirectory!!).name.ifEmpty { selectedDirectory } else "Select project directory",
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.Text,
                fontSize = 11.sp,
                fontWeight = if (hasDirectory) FontWeight.Medium else FontWeight.Normal,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = if (hasDirectory) "Click to change" else "Click to browse",
                modifier = Modifier.fillMaxWidth(),
                color = AppColors.TextMuted,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
