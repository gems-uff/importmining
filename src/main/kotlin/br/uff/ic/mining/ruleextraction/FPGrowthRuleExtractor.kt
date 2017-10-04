package br.uff.ic.mining.ruleextraction

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import weka.associations.FPGrowth
import weka.core.Instances
import weka.core.Tag

class FPGrowthRuleExtractor(
        val minimumSupport: Double,
        val maximumSupport: Double,
        val supportStep: Double,
        val targetMetric: Metric,
        val minimumTargetMetricValue: Double,
        val numberOfRulesToFind: Int
) : RuleExtractor {
    private companion object : Logger by LoggerFactory.new(FPGrowthRuleExtractor::class.java.canonicalName)
    override fun extract(dataSet: Instances) {
        val fpGrowth = FPGrowth()
        fpGrowth.lowerBoundMinSupport = minimumSupport
        fpGrowth.upperBoundMinSupport = maximumSupport
        fpGrowth.delta = supportStep
        fpGrowth.minMetric = minimumTargetMetricValue
        Tag()
        fpGrowth.metricType = targetMetric.selectTag
        if (numberOfRulesToFind == -1) {
            fpGrowth.findAllRulesForSupportLevel = true
        } else {
            fpGrowth.numRulesToFind = numberOfRulesToFind
        }
        fpGrowth.buildAssociations(dataSet)
        val rules = fpGrowth.associationRules.rules
            .filter { it.consequence.size == 1 }
            .map {
                Rule(
                    premise = it.premise.map { it.attribute.name() },
                    consequence = it.consequence.map { it.attribute.name() },
                    confidence = it.getNamedMetricValue("Confidence"),
                    conviction = it.getNamedMetricValue("Conviction"),
                    lift = it.getNamedMetricValue("Lift"),
                    leverage = it.getNamedMetricValue("Leverage"),
                    support = it.totalSupport.toDouble() / dataSet.size
                )
            }
        debug(rules.joinToString("\n"))
    }
}