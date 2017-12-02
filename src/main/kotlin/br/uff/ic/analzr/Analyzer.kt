package br.uff.ic.analzr

interface Analyzer<in R, out E> {
    fun analyze(basis : R, projectRoot : String) : E?
}