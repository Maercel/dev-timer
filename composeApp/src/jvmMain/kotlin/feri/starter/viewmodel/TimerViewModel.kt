package feri.starter.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import feri.starter.CodeLinesCounter
import feri.starter.DatabaseFactory
import feri.starter.SoundPlayer
import feri.starter.Developer
import feri.starter.model.DeveloperState
import feri.starter.model.ProjectState
import feri.starter.model.SessionState
import feri.starter.model.TimerState
import feri.starter.model.TimerUIState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.time.LocalDate
import kotlin.math.abs

const val INITIAL_TIME_SECONDS = 60
const val MAX_TIME_SECONDS = 8 * 60 * 60
/*
fun generateSessions(developer: Developer): List<Session> {
    val faker = Faker()

    val projects = List(3) {
        Project(
            id = it,
            name = faker.app.name(),
            directoryPath = "C:/Projects/${faker.name}"
        )
    }

    val sessionList = mutableListOf<Session>()

    repeat(200) {
        val randomProject = projects.random()

        val dayOfYear = Random.nextInt(1, 366)
        val randomDate = LocalDate.ofYearDay(2026, dayOfYear)

        val randomLines = Random.nextLong(10, 1000)

        sessionList.add(
            Session(
                id = it,
                developer = developer,
                project = randomProject,
                date = randomDate,
                linesAdded = randomLines
            )
        )
    }

    return sessionList.sortedBy {it.date}
}

 */

class TimerViewModel : ViewModel() {
    //private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timerJob: Job? = null // cancel, join, states

    val factory = DatabaseFactory()
    val db = factory.database


    // fixed developer
    val currentDeveloperState = DeveloperState(id = 0, name = "Marcel")

    private val _uiState = MutableStateFlow(TimerUIState())
    val uiState = _uiState.asStateFlow()

    val sessionStates = mutableStateListOf<SessionState>()

    init {
        ensureDeveloperExists()
        loadSessions()
    }

    private fun ensureDeveloperExists() {
        val existing = db.developerQueries.getDeveloperById(currentDeveloperState.id).executeAsOneOrNull()
        if (existing == null) {
            db.developerQueries.addDeveloper(Developer(currentDeveloperState.id, currentDeveloperState.name))
        }
    }

    private fun loadSessions() {
        sessionStates.clear()
        // had to do it because of type missmatch (i think)
        val dbSessions = db.sessionQueries.getAllSessionsWithDetails().executeAsList()

        val mappedSessionStates = dbSessions.map { dbSession ->
            val dev = DeveloperState(id = dbSession.developerId, name = dbSession.developerName)
            val projectState = ProjectState(
                id = dbSession.projectId,
                name = dbSession.projectName,
                directoryPath = dbSession.projectPath
            )

            SessionState(
                id = dbSession.sessionId,
                developerState = dev,
                projectState = projectState,
                date = dbSession.sessionDate,
                linesAdded = dbSession.linesAdded
            )
        }

        sessionStates.addAll(mappedSessionStates)
    }


    private var initialLineCount: Deferred<Int>? = null

    fun start() {
        val state = _uiState.value
        if (state.timerState == TimerState.RUNNING) return

        val dir = state.selectedDirectory
        if (state.timerState == TimerState.IDLE && dir != null) {
            initialLineCount = viewModelScope.async(Dispatchers.IO) { CodeLinesCounter.countCodeLines(dir) }
        }

        _uiState.update { it.copy(timerState = TimerState.RUNNING) }
        timerJob = viewModelScope.launch { runTimer() }
    }

    fun pause() {
        timerJob?.cancel()
        _uiState.update {
            it.copy(timerState = TimerState.PAUSED)
        }
    }

    fun stop() {
        timerJob?.cancel()
        val state = _uiState.value
        val dir = state.selectedDirectory
        val hasRun = state.timerState != TimerState.IDLE && state.timerSecondsLeft < state.timerSecondsTotal
        val total = state.timerSecondsTotal
        _uiState.value = TimerUIState(
            selectedDirectory = dir,
            timerSecondsLeft = total,
            timerSecondsTotal = total
        )

        if (hasRun && dir != null) {
            val baseline = initialLineCount
            viewModelScope.launch { saveSession(dir, baseline) }
        }
    }

    fun setDuration(seconds: Int) {
        if (_uiState.value.timerState != TimerState.IDLE) return
        _uiState.update {
            it.copy(timerSecondsTotal = seconds, timerSecondsLeft = seconds)
        }
    }

    fun addTime(seconds: Int) {
        if (_uiState.value.timerState != TimerState.IDLE) return
        val total = (_uiState.value.timerSecondsTotal + seconds).coerceAtMost(MAX_TIME_SECONDS)
        setDuration(total)
    }

    fun selectDirectory(path: String) {
        _uiState.update {
            it.copy(selectedDirectory = path)
        }
    }

    private suspend fun runTimer() {
        while (_uiState.value.timerState == TimerState.RUNNING && _uiState.value.timerSecondsLeft > 0) {
            delay(1000L)
            _uiState.update {
                it.copy(timerSecondsLeft = it.timerSecondsLeft - 1)
            }
        }

        if (_uiState.value.timerSecondsLeft == 0 && _uiState.value.timerState == TimerState.RUNNING) {
            onTimerFinished()
        }
    }

    private suspend fun onTimerFinished() {
        viewModelScope.launch { SoundPlayer.playDone() }
        val dir = _uiState.value.selectedDirectory
        if (dir != null) {
            saveSession(dir, initialLineCount)
        }

        val total = _uiState.value.timerSecondsTotal
        _uiState.value = TimerUIState(
            selectedDirectory = dir,
            timerSecondsLeft = total,
            timerSecondsTotal = total
        )
    }

    private suspend fun saveSession(dir: String, baseline: Deferred<Int>?) {
        val finalLineCount = withContext(Dispatchers.IO) { CodeLinesCounter.countCodeLines(dir) }
        val startLineCount = baseline?.await() ?: finalLineCount
        val difference = finalLineCount - startLineCount
        // database
        //val project = Project(id = 0, name = File(dir).name, directoryPath = dir) // temp

        // create new project
        var dbProject = db.projectQueries.getProjectByPath(dir).executeAsOneOrNull()
        if (dbProject == null) {
            db.projectQueries.addProject(name = File(dir).name, directory_path = dir)
            dbProject = db.projectQueries.getProjectByPath(dir).executeAsOne() // reading id of the project
        }
        /*
        sessions.add(Session(
            id = 0, // temporary
            developer = currentDeveloper,
            project = project,
            date = LocalDate.now(),
            linesAdded = difference.toLong()
        ))
         */

        db.sessionQueries.addSession(
            developer_id = currentDeveloperState.id,
            project_id = dbProject.id,
            local_date = LocalDate.now(),
            lines_added = difference.toLong()
        )

        loadSessions() // reading fresh sessions

        println("Lines changed: $difference")
    }
}
