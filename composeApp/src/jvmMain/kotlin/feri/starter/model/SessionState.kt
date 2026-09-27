package feri.starter.model

import java.time.LocalDate

data class SessionState(
    val id: Int,
    val developerState: DeveloperState,
    val projectState: ProjectState,
    val date: LocalDate,
    val linesAdded: Long
)