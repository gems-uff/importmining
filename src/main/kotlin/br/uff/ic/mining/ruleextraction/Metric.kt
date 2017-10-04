package br.uff.ic.mining.ruleextraction

import weka.associations.DefaultAssociationRule
import weka.core.SelectedTag
import weka.core.Tag

enum class Metric(private val identifier: Int) {
    Confidence(DefaultAssociationRule.METRIC_TYPE.values().first { it.name.toLowerCase() == "confidence" }.ordinal),
    Lift(DefaultAssociationRule.METRIC_TYPE.values().first { it.name.toLowerCase() == "lift" }.ordinal),
    Conviction(DefaultAssociationRule.METRIC_TYPE.values().first { it.name.toLowerCase() == "conviction" }.ordinal),
    Leverage(DefaultAssociationRule.METRIC_TYPE.values().first { it.name.toLowerCase() == "leverage" }.ordinal);

    val tag: Tag
        get() {
            return Tag(this.identifier, this.name)
        }
    val selectTag: SelectedTag
        get() {
            return SelectedTag(0, arrayOf(this.tag))
        }
}