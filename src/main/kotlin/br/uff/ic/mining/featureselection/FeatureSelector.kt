package br.uff.ic.mining.featureselection

import weka.core.Instances

interface FeatureSelector {
    fun select(dataSet: Instances): Instances

    companion object {
        fun new(specs: List<Any>): FeatureSelector {
            val selectors = specs.map { spec ->
                if (spec is Map<*, *>) {
                    val selectorName = spec.keys.first() as String
                    when (selectorName.toLowerCase()) {
                        "attributeremover" -> {
                            val attributes = spec.values.first()
                            if (attributes is List<*>) {
                                if (attributes.any { it !is String }) {
                                    error("The given arguments were NOT a list of strings!")
                                }
                                AttributeRemover(*attributes.map { it.toString() }.toTypedArray())
                            } else {
                                error("The given arguments were NOT a list of strings!")
                            }
                        }
                        "everyotherattributeremover" -> {
                            val attributes = spec.values.first()
                            if (attributes is List<*>) {
                                if (attributes.any { it !is String }) {
                                    error("The given arguments were NOT a list of strings!")
                                }
                                EveryOtherAttributeRemover(*attributes.map { it.toString() }.toTypedArray())
                            } else {
                                error("The given arguments were NOT a list of strings!")
                            }
                        }
                        else -> {
                            error("'$selectorName' is NOT a valid feature selector")
                        }
                    }
                } else {
                    error("Bad specification! It should be a key-value structure")
                }
            }
            return PipelineFeatureSelector(*selectors.toTypedArray())
        }
    }
}
