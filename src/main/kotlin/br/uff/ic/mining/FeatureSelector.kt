package br.uff.ic.mining

import weka.core.Instances

interface FeatureSelector {
    fun select(dataSet: Instances): Instances
}