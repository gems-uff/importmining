package br.uff.ic.mining

import br.uff.ic.extensions.conviction
import br.uff.ic.extensions.lift
import br.uff.ic.extensions.support
import org.apache.spark.mllib.fpm.AssociationRules

data class Rule(
        val premises: List<String>,
        val consequent: List<String>,
        val support: Double,
        val conviction: Double,
        val confidence: Double,
        val lift: Double
) {
    companion object {
        fun fromSparkRule(rule: AssociationRules.Rule<String>, dataSet: DataSet): Rule {
            return Rule(
                    premises = rule.javaAntecedent().toList(),
                    consequent = rule.javaConsequent().toList(),
                    confidence = rule.confidence(),
                    lift = rule.lift(dataSet),
                    support = rule.support(dataSet),
                    conviction = rule.conviction(dataSet)
            )
        }
    }
}