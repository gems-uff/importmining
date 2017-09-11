package br.uff.ic.mining.ruleextraction

import weka.associations.FPGrowth
import weka.core.Instances

class FPGrowthRuleExtractor(
        val minimumSupport: Double,
        val maximumSupport: Double,
        val supportStep: Double,
        val targetMetric: Metric,
        val minimumTargetMetricValue: Double,
        val numberOfRulesToFind: Int
) : RuleExtractor {
    override fun extract(dataSet: Instances) {
        val fpg = FPGrowth()
        fpg.numRulesToFind = numberOfRulesToFind
        fpg.minMetric = minimumTargetMetricValue
        fpg.lowerBoundMinSupport = minimumSupport

    }
}