package br.uff.ic.mining.ruleextraction

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import weka.core.Instances

class MultipleAlgorithmRuleExtractor(
        private val numberOfWorks: Int,
        private vararg val extractors: RuleExtractor
) : RuleExtractor {
    val numberOfAlgorithms: Int = extractors.size

    override fun extract(dataSet: Instances) {
        val semaphore = Channel<Boolean>(numberOfWorks)
        extractors.forEach { extractor ->
            launch(CommonPool) {
                semaphore.send(true)
                extractor.extract(dataSet)
                semaphore.receive()
            }
        }
    }

}