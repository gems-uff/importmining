package br.uff.ic.pipelines

import br.uff.ic.collector.ImportCollector
import br.uff.ic.mining.Rule
import br.uff.ic.mining.RuleExtractor
import br.uff.ic.vcs.VCS
import kotlinx.coroutines.experimental.runBlocking


class ExtractRulesPipeline(
        private val vcs: VCS,
        private val collector: ImportCollector,
        private val extractor: RuleExtractor,
        checkpoints: Bucket
) : Pipeline<String, Set<Rule>>(
        checkpoints
) {
    override fun execute(input: String): List<Rule> {
        println("cloning")
        val src = load("${input.hashCode()}-cloned-project-path") {
            vcs.clone(input).absolutePath
        }
        println("cloned")
        val dataSet = load("${input.hashCode()}-imports-collected") {
            runBlocking {
                collector.collect(src)
            }
        }
        println("collected")
        return load("${dataSet.hashCode()}-${extractor.hashCode()}-rules-extracted") {
            extractor.extract(dataSet)
        }
    }
}

