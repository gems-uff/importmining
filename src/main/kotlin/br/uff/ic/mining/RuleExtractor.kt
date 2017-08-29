package br.uff.ic.mining

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import weka.associations.Apriori
import weka.core.Instances
import weka.core.converters.CSVLoader
import java.io.File

interface RuleExtractor {
    fun extract(dataSet: Instances)
    fun extract(csv: String) {
        val loader = CSVLoader()
        loader.setSource(File(csv))
        val dataSet = loader.dataSet
        dataSet.setClassIndex(-1)
        extract(loader.dataSet)
    }
}


class WekaRulerExtractor(
    private val featureSelector: FeatureSelector
) : RuleExtractor {
    private companion object : Logger by LoggerFactory.new(WekaRulerExtractor::class.java.canonicalName)

    override fun extract(dataSet: Instances) {
        val filteredDataSet = featureSelector.select(dataSet)
        val apriori = Apriori()
        apriori.lowerBoundMinSupport = 0.3
        apriori.significanceLevel = 0.7
        apriori.buildAssociations(filteredDataSet)
        debug(apriori.toString())
    }
}