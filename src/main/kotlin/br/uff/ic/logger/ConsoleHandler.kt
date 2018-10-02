package br.uff.ic.logger

import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Class responsible for handling the logging to stdout, according to the given pattern
 *
 * @param minimalLevel the level considered useful for the current execution
 * @param template the template used to printing
 * */
class ConsoleHandler(private val minimalLevel: Severity = Severity.Debug,
                     private val template: String = "{timestamp} {severity} {owner} {message}") : LogHandler {
    /**
     * Logs @param message if @param severity is "relevant"
     * */
    override fun handle(severity: Severity, owner: String, message: String) {
        if (severity.identifier <= minimalLevel.identifier) {
            return println(template
                    .replace("{timestamp}", LocalDateTime.now().toString())
                    .replace("{owner}", owner)
                    .replace("{severity}", severity.name.toUpperCase())
                    .replace("{message}", message)
            )
        }
    }
}