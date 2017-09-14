package br.uff.ic.mining.ruleextraction

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import weka.associations.DefaultAssociationRule
import weka.associations.FPGrowth
import weka.core.Instances
import weka.core.SelectedTag
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
        val fpg = FPGrowth()
        fpg.numRulesToFind = numberOfRulesToFind
        fpg.minMetric = minimumTargetMetricValue
        fpg.lowerBoundMinSupport = minimumSupport
        fpg.upperBoundMinSupport = maximumSupport
        fpg.delta = supportStep

        val tags : Array<Tag> = Metric.values().map { Tag(it.ordinal, it.name) }.toTypedArray()

        fpg.metricType = SelectedTag(targetMetric.ordinal, tags)

        fpg.buildAssociations(dataSet)
        debug(fpg.toString())
    }
}