package br.uff.ic.mining.featureselection

data class DataSet(
        val header: List<String>,
        val data: List<Pair<String, List<Int>>>
) {
    override fun toString(): String {
        return data.joinToString("\n") { (_, imports) ->
            imports.joinToString(" ")
        }
    }

    fun deleteAttribute(attribute: String) {

    }
}