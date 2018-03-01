package br.uff.ic.mining

import org.apache.spark.api.java.JavaRDD

/**
 * Class responsible for storing the model to be mined, holding classes and the imports to each
 * */
data class DataSet(val header: List<String>, val data: Collection<Transaction>) {
    fun supportOf(set: Set<String>): Double {
        return data.filter { (_, row) -> row.containsAll(set) }.size.toDouble() / data.size
    }

    fun toRDD(): JavaRDD<List<String>> =
            this.data
                .map { (_, imp) -> imp.toList() }
                .let { return ImportMining.sparkContext.parallelize(it) }
}