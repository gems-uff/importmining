package br.uff.ic.extensions

import br.uff.ic.mining.DataSet
import org.apache.spark.mllib.fpm.AssociationRules


fun <T> AssociationRules.Rule<T>.conviction(dataSet: DataSet): Double {
    return (1 - dataSet.supportOf(this.javaConsequent().toStringSet())) / (1 - this.confidence())
}

fun <T> AssociationRules.Rule<T>.support(dataSet: DataSet): Double {
    return dataSet.supportOf(this.javaAntecedent().toStringSet())
}


fun <T> AssociationRules.Rule<T>.lift(dataSet: DataSet): Double {
    val antecedent = this.javaAntecedent().toStringSet()
    val consequent = this.javaConsequent().toStringSet()
    val suppAntecedent = dataSet.supportOf(antecedent)
    val suppConsequent = dataSet.supportOf(consequent)
    val suppUnion = dataSet.supportOf(antecedent.union(consequent))
    return suppUnion / (suppAntecedent * suppConsequent)
}