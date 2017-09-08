package br.uff.ic.mining.ruleextraction

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}