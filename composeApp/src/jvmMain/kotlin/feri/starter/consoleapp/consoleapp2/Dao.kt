package feri.starter.consoleapp.consoleapp2

interface Dao<T> {
    fun getById(id: Int): T?
    fun getAll(): MutableList<T>
    fun insert(entity: T): Boolean
    fun update(entity: T): Boolean
    fun delete(entity: T): Boolean
    fun writeAll(entities: MutableList<T>)
}