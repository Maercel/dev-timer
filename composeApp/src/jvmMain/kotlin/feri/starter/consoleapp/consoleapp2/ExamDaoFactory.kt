package feri.starter.consoleapp.consoleapp2

object ExamDaoFactory {
    enum class StorageType { JSON, XML}

    fun getExamDao(type: StorageType, filePath: String = "") = when (type) {
        StorageType.XML -> if (filePath.isNotBlank()) ExamDAOXml(filePath) else ExamDAOXml()
        StorageType.JSON -> if (filePath.isNotBlank()) ExamDAOJson(filePath) else ExamDAOJson()
    }
}