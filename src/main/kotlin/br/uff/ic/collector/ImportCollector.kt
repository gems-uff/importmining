package br.uff.ic.collector

import br.uff.ic.mining.DataSet

interface ImportCollector {
    fun collect(root: String): DataSet
}