@file:UseSerializers(LocalDateTimeSerializer::class)

package feri.starter.consoleapp.consoleapp2

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime


@Serializable
data class Exam (val id: Int, val subject: String,
            //@Serializable (with= LocalDateTimeSerializer::class)
            var availableFrom: LocalDateTime,
            //@Serializable (with= LocalDateTimeSerializer::class)
            var availableUntil: LocalDateTime,
            var appliedStudents: MutableList<String> = mutableListOf()
){
    init {
        require(availableUntil.isAfter(availableFrom)) {"D" +
                "Date availableFrom must be before availableUntil. "}
    }

    fun isOpen(): Boolean {
        val now = LocalDateTime.now()
        return now.isAfter(availableFrom) && now.isBefore(availableUntil)
    }

    fun apply(studentId: String): Boolean {
        if (!isOpen()) return false
        appliedStudents.add(studentId)
        return true
    }
}