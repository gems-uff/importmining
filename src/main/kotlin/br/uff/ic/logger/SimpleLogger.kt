package br.uff.ic.logger


class SimpleLogger(private val owner: String, private val handlers: Iterable<LogHandler>) : Logger {

    override fun log(severity: Severity, message: String) {
        handlers.forEach { it.handle(severity, owner, message) }
    }
}