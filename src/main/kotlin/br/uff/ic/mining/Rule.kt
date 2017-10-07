package br.uff.ic.mining

import br.uff.ic.extensions.conviction
import br.uff.ic.extensions.lift
import br.uff.ic.extensions.support
import org.apache.spark.mllib.fpm.AssociationRules
import kotlin.streams.toList

data class Rule(
    val premises: List<String>,
    val consequent: List<String>,
    val support: Double,
    val conviction: Double,
    val confidence: Double,
    val lift: Double,
    val instances: List<String>
) {
    companion object {
        fun fromSparkRule(rule: AssociationRules.Rule<String>, dataSet: DataSet): Rule {
            val premises = rule.javaAntecedent().toList()
            val consequent = rule.javaConsequent().toList()
            val items = (premises + consequent).toSet()
            return Rule(
                premises = premises,
                consequent = consequent,
                    confidence = rule.confidence(),
                    lift = rule.lift(dataSet),
                    support = rule.support(dataSet),
                conviction = rule.conviction(dataSet),
                instances = dataSet.data.parallelStream().filter {
                    it.set.containsAll(items)
                }.map {
                    it.name
                }.toList()
            )
        }
    }
}