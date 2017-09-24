package br.uff.ic.mining.ruleextraction

import br.uff.ic.mining.featureselection.DataSet
import br.uff.ic.mining.featureselection.Filter
import weka.core.Instances

interface RuleExtractor {
    fun extract(dataSet: DataSet): Set<Rule>
}


