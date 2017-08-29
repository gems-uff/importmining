package br.uff.ic.mining

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import weka.attributeSelection.CfsSubsetEval
import weka.attributeSelection.GreedyStepwise
import weka.core.Instances
import weka.filters.Filter
import weka.filters.supervised.attribute.AttributeSelection

class WekaFeatureSelection : FeatureSelector {
    private companion object : Logger by LoggerFactory.new(WekaRulerExtractor::class.java.canonicalName)

    override fun select(dataSet: Instances): Instances {
        val eval = CfsSubsetEval()
        val search = GreedyStepwise()
        search.searchBackwards = true
        val filter = AttributeSelection()
        filter.evaluator = eval
        filter.search = search
        filter.setInputFormat(dataSet)
        val filteredDataSet = Filter.useFilter(dataSet, filter)
        debug("Were selectd #${filteredDataSet.numAttributes()} attributes out #${dataSet.numAttributes()}")
        return filteredDataSet
    }
}