package feri.starter.consoleapp.consoleapp1

object EventRepository {
    private var events = mutableListOf<Event>()

    fun createEvent(newEvent: Event) {
        events.add(newEvent)
    }

    fun eventsList(): List<Event> = events

    fun registerParticipantOnEvent(eventName: String, participantEmail: String)
        : RegisterResult {
        val event = events.filterIsInstance<EventManager>()
            .firstOrNull { it.name == eventName }
            ?: throw IllegalArgumentException("Event $eventName not found.")

        if (event.isParticipating(participantEmail)) return RegisterResult.AlreadyRegistered


        // registerParticipant METHOD checks if participant is participating too!
        // Could return false, because it participates
        // IS COMMENTED OUT
        return if (event.registerParticipant(participantEmail)) {
            RegisterResult.Success
        } else {
            RegisterResult.Full(event.waitingListCount())
        }

    }

    fun unRegisterParticipantOffEvent(eventName: String, participantEmail: String)
        : RegisterResult {

        val event = events.filterIsInstance<EventManager>()
            .firstOrNull { it.name == eventName }
            ?: throw IllegalArgumentException("Event $eventName not found.")

        if (!event.isParticipating(participantEmail)) return RegisterResult.NotFound

        event.unregisterParticipant(participantEmail)
        return RegisterResult.Success
    }
}