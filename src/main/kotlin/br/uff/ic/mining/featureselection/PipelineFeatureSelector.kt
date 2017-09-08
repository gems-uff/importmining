package br.uff.ic.mining.featureselection

import weka.core.Instances

class PipelineFeatureSelector(
        private vararg val selectors: FeatureSelector
) : FeatureSelector {
    val numberOfSteps: Int = selectors.size

    override fun select(dataSet: Instances): Instances {
        var filteredDataSet = dataSet
        selectors.forEach {
            filteredDataSet = it.select(filteredDataSet)
        }
        return filteredDataSet
    }
}