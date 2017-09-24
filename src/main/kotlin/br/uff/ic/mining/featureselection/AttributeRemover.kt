package br.uff.ic.mining.featureselection

class AttributeRemover(
        vararg attributes: String
) : Filter {
    private val attributes: Set<String> = attributes.toSet()
    override fun select(dataSet: DataSet): DataSet {
        val filtered = dataSet.copy()
        filtered.header.toList().filter {
            attributes.contains(it)
        }.forEach {
            filtered.deleteAttribute(it)
        }
        return filtered
    }
}