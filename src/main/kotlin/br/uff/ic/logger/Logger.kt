package br.uff.ic.logger

interface Logger {
    fun debug(message: String) {
        log(Severity.Debug, message)
    }

    fun info(message: String) {
        log(Severity.Info, message)
    }

    fun warn(message: String) {
        log(Severity.Warn, message)
    }

    fun error(message: String) {
        log(Severity.Error, message)
    }

    fun fatal(message: String) {
        log(Severity.Fatal, message)
    }

    fun log(severity: Severity, message: String)
}