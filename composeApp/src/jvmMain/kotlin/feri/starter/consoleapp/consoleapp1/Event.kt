package feri.starter.consoleapp.consoleapp1

import java.time.LocalDateTime

interface Event {

    var name: String
    var startDateTime: LocalDateTime
    var endDateTime: LocalDateTime

    fun description(): String
}