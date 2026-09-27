package feri.starter.consoleapp.consoleapp1

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Full(val waitingPosition: Int) : RegisterResult()
    object AlreadyRegistered : RegisterResult()
    object NotFound : RegisterResult()
}