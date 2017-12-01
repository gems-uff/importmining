package br.uff.ic.analzr

import br.uff.ic.mining.Rule


interface Analyzer<in E> {
    fun analyze(evidence : E, projectRoot : String) : Boolean
}