package br.uff.ic.logger

/**
 * Singleton responsible for giving a logger to be used, keeping track of which means to log with.
 * */
object LoggerFactory {
    private val handlers = mutableListOf<LogHandler>()

    /**
     * Adds a handler to log with
     * eg. ConsoleHandler
     * */
    fun addHandler(handler: LogHandler) {
        handlers.add(handler)
    }

    /**
     * gets the simplest logger possible, the one who routs logs to every handler available
     * */
    fun new(owner: Class<*>): Logger {
        return object : Logger{
            override fun log(severity: Severity, message: String) {
                handlers.forEach { it.handle(severity, owner.canonicalName, message) }
            }
        }
    }
}