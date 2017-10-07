package br.uff.ic.mining

interface RuleExtractor {
    fun extract(dataSet: DataSet): Knowledge
}


