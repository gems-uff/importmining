package br.uff.ic.mining.ruleextraction

import weka.core.Instances

interface RuleExtractor {
    fun extract(dataSet: Instances)

    companion object {
        fun new(spec: Map<Any, Any>): RuleExtractor {
            val workers = spec["workers"]
            val numberOfWorkers = when (workers) {
                null -> 1
                is Int -> workers
                else -> error("The field 'numberOfRulesToFind' must be a integer number")
            }

            val extractors = spec.filter { it.key != "workers" }.map { (extractorName, args) ->
                extractorName as String
                when (extractorName.toLowerCase()) {
                    "fpgrowth" -> {
                        newFPGrowth(args)
                    }
                    else -> {
                        error("'$extractorName' is NOT a valid rule extractor")
                    }
                }
            }
            return MultipleAlgorithmRuleExtractor(numberOfWorkers, *extractors.toTypedArray())
        }

        private fun newFPGrowth(args: Any): RuleExtractor {
            return if (args is Map<*, *>) {
                val minimumSupport: Double
                val maximumSupport: Double
                val supportStep: Double
                val support = args["support"]
                if (support != null) {
                    if (support is Map<*, *>) {
                        val minimum = support["minimum"]
                        minimumSupport = when (minimum) {
                            null -> 0.1
                            is Double -> minimum
                            is Int -> minimum.toDouble()
                            else -> error("The field 'support.minimum' must be a real number")
                        }
                        val maximum = support["maximum"]
                        maximumSupport = when (maximum) {
                            null -> 1.0
                            is Double -> maximum
                            is Int -> maximum.toDouble()
                            else -> error("The field 'support.maximum' must be a real number")
                        }
                        val step = support["step"]
                        supportStep = when (step) {
                            null -> 0.01
                            is Double -> step
                            is Int -> step.toDouble()
                            else -> error("The field 'support.step' must be a real number")
                        }
                    } else {
                        error("Bad arguments! It should be a key-value structure")
                    }
                } else {
                    minimumSupport = 0.1
                    maximumSupport = 1.0
                    supportStep = 0.01
                }
                val targetMetric: Metric
                val minimumTargetMetricValue: Double
                val metric = args["targetMetric"]
                if (metric != null) {
                    if (metric is Map<*, *>) {
                        val name = metric["name"]
                        targetMetric = when (name) {
                            null -> Metric.Confidence
                            is String -> Metric.valueOf(name.capitalize())
                            else -> error("The field 'targetMetric.name' must be a string")
                        }
                        val minimum = metric["minimum"]
                        minimumTargetMetricValue = when (minimum) {
                            null -> 0.9
                            is Double -> minimum
                            else -> error("The field 'targetMetric.minimum' must be a real number")
                        }
                    } else {
                        error("Bad arguments! It should be a key-value structure")
                    }
                } else {
                    targetMetric = Metric.Confidence
                    minimumTargetMetricValue = 0.9
                }
                val numberOfRulesToFind: Int
                val number = args["numberOfRulesToFind"]
                numberOfRulesToFind = when (number) {
                    null -> 10
                    is Int -> number
                    else -> error("The field 'numberOfRulesToFind' must be a integer number")
                }
                FPGrowthRuleExtractor(
                        minimumSupport = minimumSupport,
                        maximumSupport = maximumSupport,
                        minimumTargetMetricValue = minimumTargetMetricValue,
                        targetMetric = targetMetric,
                        supportStep = supportStep,
                        numberOfRulesToFind = numberOfRulesToFind
                )
            } else {
                error("Bad arguments! It should be a key-value structure")
            }
        }
    }
}


