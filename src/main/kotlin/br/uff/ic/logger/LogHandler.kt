package br.uff.ic.logger

interface LogHandler {
    fun handle(severity: Severity, owner: String, message: String)
}