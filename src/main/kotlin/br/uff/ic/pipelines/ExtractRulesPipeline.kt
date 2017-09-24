package br.uff.ic.pipelines

import br.uff.ic.collector.ImportCollector
import br.uff.ic.mining.featureselection.Filter
import br.uff.ic.mining.ruleextraction.Rule
import br.uff.ic.mining.ruleextraction.RuleExtractor
import br.uff.ic.vcs.VCS
import kotlinx.coroutines.experimental.runBlocking


class ExtractRulesPipeline(
        private val vcs: VCS,
        private val collector: ImportCollector,
        private val filter: Filter,
        private val extractor: RuleExtractor,
        checkpoints: Bucket
) : Pipeline<String, Set<Rule>>(
        checkpoints
) {
    override fun execute(input: String): Set<Rule> {
        val src = load("cloned-project-path") {
            vcs.clone(input).absolutePath
        }
        val dataSet = load("imports-collected") {
            runBlocking {
                collector.collect(src)
            }
        }
        val filteredDataSet = load("features-selected") {
            filter.select(dataSet)
        }
        return load("rules-extracted") {
            extractor.extract(filteredDataSet)
        }
    }
}

