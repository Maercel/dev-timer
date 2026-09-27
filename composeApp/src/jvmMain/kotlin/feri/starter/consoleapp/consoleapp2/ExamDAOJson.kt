package feri.starter.consoleapp.consoleapp2

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

const val DEFAULT_FILE_NAME = "examsData.json"

class ExamDAOJson (val fileName: String = DEFAULT_FILE_NAME): Dao<Exam> {

    private val json = Json { prettyPrint = true } // for better readability
    private val serializer = ListSerializer(Exam.serializer())


    fun readAll(): MutableList<Exam> {
        val file = File(fileName)
        if (!file.exists() || file.readText().isBlank()) return  mutableListOf();
        return json.decodeFromString(serializer, file.readText()).toMutableList();
    }

    override fun writeAll(entities: MutableList<Exam>) {
        // depends on how you run it! Application has different path than composeApp!
        //println("Writing to: ${File(fileName).absolutePath}")
        //exams.sortBy { it.id } //
        File(fileName).writeText(json.encodeToString(serializer, entities))
    }

    override fun getById(id: Int): Exam? {
        return readAll().find { it.id == id }
    }
    override fun getAll(): MutableList<Exam> {
        return readAll()
    }
    override fun insert(entity: Exam): Boolean {
        val exams = readAll()

        if (exams.any { it.id == entity.id }) return false // id exists

        exams.add(entity)
        //exams.sortBy { it.id }

        writeAll(exams)
        return true;
    }

    override fun update(entity: Exam): Boolean {
        val exams = readAll()

        val elementIndex = exams.indexOfFirst { it.id == entity.id }
        if (elementIndex == -1) return false

        exams[elementIndex] = entity

        writeAll(exams)
        return true;
    }
    
    override fun delete(entity: Exam): Boolean {
        val exams = readAll()

        val elementIndex = exams.indexOfFirst { it.id == entity.id }
        if (elementIndex == -1) return false

        exams.removeAt(elementIndex)
        writeAll(exams)
        return true;
    }
}