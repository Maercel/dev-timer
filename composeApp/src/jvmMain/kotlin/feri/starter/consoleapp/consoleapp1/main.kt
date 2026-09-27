package feri.starter.consoleapp.consoleapp1

import java.time.LocalDateTime

fun displayMenu() {
    println("1) List events")
    println("2) Register user")
    println("3) Unregister user")
    println("0) Exit")
}

fun displayEvents() = EventRepository.eventsList().forEach { println(it.description()) }

fun registerMenu() {

    println("Enter event name: ")
    val eventName: String = readlnOrNull()?.trim() ?: throw IllegalArgumentException("Event name cannot be null.")

    println("Enter participant email: ")
    // requireNotNull(readlnOrNull()?.trim()) { "ble" }
    val participantName: String = readlnOrNull()?.trim() ?: throw IllegalArgumentException("Participant email cannot be null. ")

    when (val registerResult: RegisterResult = EventRepository.registerParticipantOnEvent(eventName, participantName)) {
        is RegisterResult.AlreadyRegistered -> println("Participant already registered.")
        is RegisterResult.NotFound -> println("Participant not found.")
        is RegisterResult.Full -> println("Event is full. You are #${registerResult.waitingPosition} in the waiting list. ")
        is RegisterResult.Success -> println("Participant successfully registered.")
    }
}

fun unregisterMenu() {

    println("Enter event name: ")
    val eventName: String = readlnOrNull()?.trim() ?: throw IllegalArgumentException("Event name cannot be null.")

    println("Enter participant email: ")
    // requireNotNull(readlnOrNull()?.trim()) { "ble" }
    val participantName: String =
        readlnOrNull()?.trim() ?: throw IllegalArgumentException("Participant email cannot be null. ")

    when (val registerResult: RegisterResult =
        EventRepository.unRegisterParticipantOffEvent(eventName, participantName)) {
        is RegisterResult.NotFound -> println("Participant not found.")
        is RegisterResult.Success -> println("Participant successfully unregistered.")
        else -> println("Something went wrong in EventRepository.unRegisterParticipantOnEvent(eventName, participantName)")
    }
}


fun main() {
    var codingConference: Conference =
        Conference("Coding Conference",
            LocalDateTime.of(2026, 5, 10, 9, 0),
            LocalDateTime.of(2026, 5, 10, 17, 0),
            400,
            "Marcel"
        )
    var codingWorkshop: Workshop =
        Workshop("Coding Workshop",
            LocalDateTime.of(2026, 5, 11, 10, 0),
            LocalDateTime.of(2026, 5, 11, 17, 0),
            300,
            "English language"
        )

    var vipCodingWorkshop: Workshop =
        Workshop("VIP Coding Workshop",
            LocalDateTime.of(2026, 5, 12, 10, 0),
            LocalDateTime.of(2026, 5, 12, 17, 0),
            1,
            "English language"
        )

    var testWorkshop: Workshop =
        Workshop("Test Workshop",
            LocalDateTime.of(2026, 3, 16, 8, 0),
            LocalDateTime.of(2026, 3, 16, 17, 0),
            2,
            "English language"
    )

    EventRepository.createEvent(codingConference)
    EventRepository.createEvent(codingWorkshop)
    EventRepository.createEvent(vipCodingWorkshop)
    EventRepository.createEvent(testWorkshop)

    /*
    try {
        Workshop(
            name = "InvalidWorkshop",
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(2),
            capacity = 0,
            prerequisites = "None"
        )
    } catch (e: IllegalArgumentException) {
        println("Exception caught: ${e.message}")
        return
    }
    */

    while (true) {
        try {
        displayMenu()

        when (readlnOrNull()?.trim()) {
            "1" -> displayEvents()
            "2" -> registerMenu()
            "3" -> unregisterMenu()
            "0" -> break
            else -> println("Invalid input.")
        }
        } catch (e: IllegalArgumentException) {
            println("Exception: ${e.message}")
        } catch (e: IllegalStateException) {
            println("Exception: ${e.message}")
        }
    }

}