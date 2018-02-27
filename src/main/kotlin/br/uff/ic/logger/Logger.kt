package br.uff.ic.logger

/**
 * Logger interface, classes implementing the usable interface for logging should implement this
 * */
interface Logger {

    /**
     * writes @param message with Debug severity
     * */
    fun debug(message: String) {
        log(Severity.Debug, message)
    }

    /**
     * writes @param message with Info severity
     * */
    fun info(message: String) {
        log(Severity.Info, message)
    }

    /**
     * writes @param message with Warn severity
     * */
    fun warn(message: String) {
        log(Severity.Warn, message)
    }

    /**
     * writes @param message with Error severity
     * */
    fun error(message: String) {
        log(Severity.Error, message)
    }

    /**
     * writes @param message with Fatal severity
     * */
    fun fatal(message: String) {
        log(Severity.Fatal, message)
    }

    /**
     * writes @param message with @param severity
     * */
    fun log(severity: Severity, message: String)
}