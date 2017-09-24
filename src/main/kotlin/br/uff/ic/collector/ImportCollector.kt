package br.uff.ic.collector

import br.uff.ic.mining.featureselection.DataSet

interface ImportCollector {
    suspend fun collect(root: String): DataSet
}