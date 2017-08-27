package br.uff.ic.logger

import java.time.LocalTime

class ConsoleHandler(private val minimalLevel: Severity = Severity.Debug, private val template: String = "{severity}||{timestamp}||{owner}||{message}") : LogHandler {
    override fun handle(severity: Severity, owner: String, message: String) {
        if (severity.identifier <= minimalLevel.identifier) {
            return println(template
                    .replace("{timestamp}", LocalTime.now().toString())
                    .replace("{owner}", owner)
                    .replace("{severity}", severity.name.toUpperCase())
                    .replace("{message}", message)
            )
        }
    }

}