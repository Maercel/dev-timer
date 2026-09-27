package feri.starter.consoleapp.consoleapp1

import java.time.LocalDateTime

abstract class EventManager (
    override var name: String,
    override var startDateTime: LocalDateTime,
    override var endDateTime: LocalDateTime,
    var capacity: Int
    ) : Event {

    init {
        require(capacity > 0) { "Capacity must be greater than 0" }
    }

    private var participantsEmails = mutableListOf<String>()
    private var waitingList = mutableListOf<String>()

    fun isSoldOut(): Boolean = participantsEmails.size >= capacity

    fun registerParticipant(participantEmail: String): Boolean {
        //if (isParticipating(participantEmail)) return false
        if (eventStarted()) throw IllegalStateException("Registration no longer possible. Event has already started. ")
        if (!isSoldOut()) {
            participantsEmails.add(participantEmail)
            return true
        }

        waitingList.add(participantEmail)
        return false
    }

    fun unregisterParticipant(participantEmail: String) {
        if (participantsEmails.contains(participantEmail)) {
            participantsEmails.remove(participantEmail)
            if (waitingList.isNotEmpty())
                participantsEmails.add(waitingList.removeAt(0))
        }
        else if (waitingList.contains(participantEmail)) {
            waitingList.remove(participantEmail)
        }
    }

    fun eventStarted(): Boolean {
        return LocalDateTime.now().isAfter(startDateTime)
    }

    fun isParticipating(participantEmail: String): Boolean {
        return isRegistered(participantEmail) || isInWaitingList(participantEmail)
    }

    fun isRegistered(participantEmail: String): Boolean = participantEmail in participantsEmails
    fun isInWaitingList(participantEmail: String): Boolean = participantEmail in waitingList
    fun participantsEmailsCount(): Int = participantsEmails.size
    fun waitingListCount(): Int = waitingList.size

    //TODO: Implement it in other classes
    abstract override fun description(): String
}