package br.uff.ic.mining

import org.apache.spark.mllib.fpm.FPGrowth

class Itemset(sparkItemset : FPGrowth.FreqItemset<String>) {
    val items : Collection<String>
    val frequency : Long

    init {
        items = sparkItemset.javaItems()
        frequency = sparkItemset.freq()
   }
}