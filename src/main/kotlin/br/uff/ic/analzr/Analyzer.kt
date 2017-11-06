package br.uff.ic.analzr

import br.uff.ic.mining.Rule


interface Analyzer {
    fun analyze(evidence : Rule, projectRoot : String) : Boolean
}