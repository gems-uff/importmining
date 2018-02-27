package br.uff.ic.mining

import org.apache.spark.mllib.fpm.FPGrowth
import org.apache.spark.mllib.fpm.FPGrowthModel


class FPGrowthRuleExtractor(
    private val minimumSupport: Double,
    private val minimumConfidence: Double
) : RuleExtractor {
    override fun extract(dataSet: DataSet): Collection<Rule> {
        val algorithm = FPGrowth()
        algorithm.setMinSupport(minimumSupport)
        algorithm.setNumPartitions(10)
        val fpGrowthModel: FPGrowthModel<String> = algorithm.run(dataSet.toRDD())
        val rules = mutableListOf<Rule>()
        for (rule in fpGrowthModel.generateAssociationRules(minimumConfidence).toLocalIterator().toIterable()) {
            rules.add(Rule.fromSparkRule(rule, dataSet))
        }
        return rules
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FPGrowthRuleExtractor

        if (minimumSupport != other.minimumSupport) return false
        if (minimumConfidence != other.minimumConfidence) return false

        return true
    }

    override fun hashCode(): Int {
        var result = minimumSupport.hashCode()
        result = 31 * result + minimumConfidence.hashCode()
        return result
    }
}