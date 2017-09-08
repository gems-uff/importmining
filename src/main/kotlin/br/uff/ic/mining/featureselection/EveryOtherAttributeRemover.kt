package br.uff.ic.mining.featureselection

import weka.core.Instances

class EveryOtherAttributeRemover(
        private vararg val attributes: String
) : FeatureSelector {
    override fun select(dataSet: Instances): Instances {
        val toRemove = dataSet.enumerateAttributes().toList().filter {
            !attributes.map {
                it.toLowerCase()
            }.contains(it.name().toLowerCase())
        }
        toRemove.forEach {
            dataSet.deleteAttributeAt(it.index())
        }
        return dataSet
    }
}