package br.uff.ic.mining.ruleextraction

import br.uff.ic.mining.DataSet

interface RuleExtractor {
    fun extract(dataSet: DataSet): Set<Rule>
}


