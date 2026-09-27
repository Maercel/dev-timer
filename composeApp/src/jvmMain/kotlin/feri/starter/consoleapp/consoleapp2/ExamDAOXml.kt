package feri.starter.consoleapp.consoleapp2

import kotlinx.serialization.builtins.ListSerializer
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

const val DEFAULT_XML_FILE_NAME = "examsXMLData.xml"

class ExamDAOXml (val fileName: String = DEFAULT_XML_FILE_NAME) : Dao<Exam> {
    val xml = XML() {
        indentString = "    "
        xmlDeclMode = XmlDeclMode.None
        autoPolymorphic = true
    }
    private val serializer = ListSerializer(Exam.serializer())

    fun readAll(): MutableList<Exam> {
        val file = File(fileName)
        if (!file.exists() || file.readText().isBlank()) return  mutableListOf();
        return xml.decodeFromString(serializer, file.readText()).toMutableList()
    }

    override fun writeAll(entities: MutableList<Exam>) {
        val xmlString = xml.encodeToString(serializer, entities)
        File(fileName).writeText(xmlString)
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