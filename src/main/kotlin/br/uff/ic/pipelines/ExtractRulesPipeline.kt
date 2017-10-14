package br.uff.ic.pipelines

import br.uff.ic.collector.ImportCollector
import br.uff.ic.extensions.load
import br.uff.ic.extensions.toBase64
import br.uff.ic.mining.Rule
import br.uff.ic.mining.RuleExtractor
import br.uff.ic.vcs.VCS
import kotlinx.coroutines.experimental.runBlocking


class ExtractRulesPipeline(
        private val vcs: VCS,
        private val collector: ImportCollector,
        private val extractor: RuleExtractor,
        checkpoints: Bucket
) : Pipeline<String, Iterable<Rule>>(
        checkpoints
) {
    override fun execute(input: String): Iterable<Rule> {
        println("cloning")
        val src = load("${input.toBase64()}-cloned-project-path") {
            vcs.clone(input).absolutePath
        }
        println("cloned")
        val dataSet = load("${input.toBase64()}-imports-collected") {
            runBlocking {
                collector.collect(src)
            }
        }
        println("collected")
        return load("${dataSet.toBase64()}-${extractor.toBase64()}-rules-extracted") {
            extractor.extract(dataSet)
        }
    }
}

