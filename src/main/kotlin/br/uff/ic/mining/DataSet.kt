package br.uff.ic.mining

data class DataSet(
    val header: List<String>,
    val data: Collection<Row>
) {

    fun supportOf(set: Set<String>): Double {
        return data.filter { (_, row) ->
            row.containsAll(set)
        }.size.toDouble() / data.size
    }
}

