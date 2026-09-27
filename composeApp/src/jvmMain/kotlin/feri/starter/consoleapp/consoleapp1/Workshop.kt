package feri.starter.consoleapp.consoleapp1

import java.time.LocalDateTime

class Workshop (
    name: String,
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    capacity: Int,
    var prerequisites: String
    ) : EventManager(name, startDateTime, endDateTime, capacity) {

    override fun description(): String {
        return "Workshop: $name | prerequisites: $prerequisites | capacity: ${participantsEmailsCount()}/$capacity".let {
            if (waitingListCount() > 0) "$it | waiting list: ${waitingListCount()}" else it
        }
    }
}