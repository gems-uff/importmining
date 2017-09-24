package br.uff.ic.mining.featureselection

interface Filter {
    fun select(dataSet: DataSet): DataSet
}

