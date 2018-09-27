package br.uff.ic.mining

import br.uff.ic.domain.SourceFile
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.mllib.fpm.FPGrowth
import kotlin.streams.toList

/**
 * Class responsible for storing the model to be mined, holding classes and the imports to each
 * */
data class DataSet(private val imports : Set<String>, private val sourceFiles : List<SourceFile>) {
    // Must be kept in order to the parallelization to work
    @Suppress("unused")
    val header: List<String> = imports.sorted()
    val data: Collection<Transaction> = sourceFiles.parallelStream().map { Transaction(it.getFilePath(), it.imports)}.distinct().toList()

    //needed by spark
    private fun toRDD(): JavaRDD<Set<String>> =
            this.data
                    .map { (_, imp) -> imp.toSet() }
                    .let { return ImportMining.sparkContext.parallelize(it) }

    // run spark
    fun learnFrequentItemsets(minimumSupport : Double) : Collection<Itemset> =
        FPGrowth()
        .setMinSupport(minimumSupport)
        .setNumPartitions(10)
        .run(this.toRDD())
        .freqItemsets()
        .toJavaRDD()
        .collect()
        .fold(mutableListOf()){
            list: MutableList<Itemset>, freqItemset: FPGrowth.FreqItemset<String> ->
            list.add(Itemset(freqItemset))
            list
        }
}