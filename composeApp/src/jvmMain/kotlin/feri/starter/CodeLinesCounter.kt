package feri.starter

import java.io.File

object CodeLinesCounter {

    private val codeExtensions = setOf(
        "kt", "kts", "java", "scala", "groovy",
        "js", "jsx", "mjs", "cjs", "ts", "tsx", "vue", "svelte",
        "c", "h", "cpp", "cc", "hpp", "cs", "m",
        "go", "rs", "swift", "dart", "php",
        "py", "rb", "sh", "ps1", "r", "lua", "sql", "sq",
        "html", "css", "scss"
    )

    private val ignoredDirectories = setOf(
        ".git", ".gradle", ".idea", ".vscode", ".kotlin",
        "build", "out", "bin", "obj", "target", "dist",
        "node_modules", ".next", ".nuxt",
        "venv", ".venv", "__pycache__", ".pytest_cache"
    )

    fun countCodeLines(directoryPath: String): Int {
        val directory = File(directoryPath);

        if (!directory.exists() || !directory.isDirectory) return 0;

        return directory
            .walkTopDown()
            .onEnter { it == directory || it.name !in ignoredDirectories }
            .filter { it.isFile && it.extension.lowercase() in codeExtensions }
            .sumOf { file ->
                file.readLines().count { line ->
                    val trimmed = line.trim();
                    trimmed.isNotEmpty() && !trimmed.startsWith("//")
                }
            }
    }
}
