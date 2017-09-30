package br.uff.ic.extensions

import br.uff.ic.mining.DataSet
import org.apache.spark.mllib.fpm.AssociationRules


fun <T> MutableList<out T>.toStringSet(): Set<String> {
    return this.map { it.toString() }.toSet()
}

