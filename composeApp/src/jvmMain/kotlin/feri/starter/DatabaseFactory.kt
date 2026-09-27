package feri.starter

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.time.LocalDate
import java.util.Properties

val localDateAdapter = object : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return value.toString()
    }
}

class DatabaseFactory {

    private fun createDriver(): SqlDriver {
        // default installer put's this into Program Files/DevTimer/ and programs need admin rights, TL;DR: IT FAILS
        val appDir = File(System.getenv("APPDATA") ?: System.getProperty("user.home"), "DevTimer")
        appDir.mkdirs()
        val dbFile = File(appDir, "timer.db")

        val driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            properties = Properties().apply { put("foreign_keys", "true") },
            schema = Database.Schema
        )
        return driver
    }

    val database = Database(
        driver = createDriver(),
        developerAdapter = Developer.Adapter(
            idAdapter = IntColumnAdapter
        ),
        projectAdapter = Project.Adapter(
            idAdapter = IntColumnAdapter
        ),
        sessionAdapter = Session.Adapter(
            idAdapter = IntColumnAdapter,
            developer_idAdapter = IntColumnAdapter,
            project_idAdapter = IntColumnAdapter,
            local_dateAdapter = localDateAdapter
        )
    )
}