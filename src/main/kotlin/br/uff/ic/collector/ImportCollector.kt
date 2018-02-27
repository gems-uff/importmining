package br.uff.ic.collector

import br.uff.ic.domain.Project
import br.uff.ic.mining.DataSet

interface ImportCollector {
    fun collect(project: Project): DataSet
}