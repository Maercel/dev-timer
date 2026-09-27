package feri.starter.consoleapp.consoleapp1

import java.time.LocalDateTime

class Conference (
    name: String,
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    capacity: Int,
    var instructorName: String
    ) : EventManager(name, startDateTime,
        endDateTime, capacity) {


    override fun description(): String {
        return "Conference: $name | speaker: $instructorName | capacity: ${participantsEmailsCount()}/$capacity".let {
            if (waitingListCount() > 0) "$it | waiting list: ${waitingListCount()}" else it
        }
    }
}