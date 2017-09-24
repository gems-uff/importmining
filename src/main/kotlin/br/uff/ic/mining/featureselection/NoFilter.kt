package br.uff.ic.mining.featureselection

class NoFilter : Filter {
    override fun select(dataSet: DataSet): DataSet {
        return dataSet
    }
}