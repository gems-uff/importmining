package br.uff.ic.logger

/**
 * Classes handling logging on specific mediums should implement this
 * */
interface LogHandler {

    /**
     * write the @param message if the @param severity is relevant, to the @param owner's log.
     * */
    fun handle(severity: Severity, owner: String, message: String)
}