package br.uff.ic.mining

interface RuleExtractor {
    fun extract(dataSet: DataSet): List<Rule>
}

