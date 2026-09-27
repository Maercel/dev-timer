package feri.starter.consoleapp.consoleapp2

import io.github.serpro69.kfaker.Faker
import java.time.LocalDateTime


fun main() {
    val examDao: Dao<Exam> = ExamDaoFactory.getExamDao(ExamDaoFactory.StorageType.XML)

    val faker = Faker()

    for (i in 0..99) {
        val startDay = (1..351).random() // 365 - 14 (availableUntil.plusDays((1..14).random().toLong())
        val availableFrom = LocalDateTime.of(2026, 1, 1, 0, 0)
            .plusDays(startDay.toLong())
            .withHour((7..12).random())
            .withMinute(0)

        val availableUntil = availableFrom
            .plusDays((1..14).random().toLong())
            .withHour((13..18).random())

        val exam = Exam(i + 1,
            faker.educator.subject(),
            availableFrom,
            availableUntil,
            appliedStudents = mutableListOf()
        )
        if (exam.isOpen()) {
            repeat(3) {
                exam.appliedStudents.add(faker.name.firstName() + " " + faker.name.lastName())
            }
        }

        examDao.insert(exam)
    }

    println("100 Exams created! ")

    // demonstracija vseh funkcij

    val daoAltered = ExamDaoFactory.getExamDao(ExamDaoFactory.StorageType.XML, "alteredXMLExamsData.xml")
    daoAltered.writeAll(examDao.getAll())

    println("getAll() size: ${daoAltered.getAll().size}")

    val exam1 = daoAltered.getById(1)
    println("getById(1) subject: ${exam1?.subject}")


    val exam1Copy = exam1!!.copy(subject = faker.educator.subject())

    exam1Copy.apply("Marcel") // depends on if exam is open
    val updateResult = daoAltered.update(exam1Copy)

    val updatedExam1 = daoAltered.getById(1)
    println("update(exam1): $updateResult, " +
            "updated subject: ${updatedExam1?.subject ?: "null"}, " +
            "isOpen(): ${updatedExam1?.isOpen() ?: "null"}, " +
            "appliedStudents: ${updatedExam1?.appliedStudents ?: "null"}")

    val exam2 = daoAltered.getById(2)
    val exam2Copy = exam2!!.copy()

    println("delete(updatedExam1) deleted: ${daoAltered.delete(exam2Copy)}, ${daoAltered.getById(2)?.subject ?: "null"}")

}