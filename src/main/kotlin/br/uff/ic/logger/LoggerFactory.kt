package br.uff.ic.logger

object LoggerFactory {
    private val handlers = mutableListOf<LogHandler>()

    fun addHandler(handler: LogHandler) {
        handlers.add(handler)
    }

    fun new(owner: String): SimpleLogger {
        return SimpleLogger(owner, handlers)
    }
}