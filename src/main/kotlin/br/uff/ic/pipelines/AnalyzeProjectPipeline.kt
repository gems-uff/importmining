package br.uff.ic.pipelines

import br.uff.ic.analzr.DataClassAnalyzer
import br.uff.ic.analzr.Evidence
import br.uff.ic.analzr.MissingFactoryAnalyzer
import br.uff.ic.collector.ImportCollector
import br.uff.ic.extensions.load
import br.uff.ic.extensions.toBase64
import br.uff.ic.mining.Rule
import br.uff.ic.mining.RuleExtractor
import br.uff.ic.vcs.VCS
import kotlinx.coroutines.experimental.runBlocking


class AnalyzeProjectPipeline(
        private val vcs: VCS,
        private val collector: ImportCollector,
        private val extractor: RuleExtractor,
        checkpoints: Bucket
) : Pipeline<String, Iterable<Evidence>>(
        checkpoints
) {
    override fun execute(input: String): Iterable<Evidence> {
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
        val rules = load("${dataSet.toBase64()}-${extractor.toBase64()}-rules-extracted") {
            extractor.extract(dataSet)
        }
        println("extracted")
        val missingFactoryEvidences = load("missing-factory-evidences"){
            MissingFactoryAnalyzer().analyze(rules, src)
        }
        println("${missingFactoryEvidences.count()} possible cases of missing factories")
        val dataClassEvidences = load("data-class-evidences"){
            rules.mapNotNull { DataClassAnalyzer().analyze(it, src) }
        }
        println("${dataClassEvidences.count()} possible cases.")

        return missingFactoryEvidences.union(dataClassEvidences)
    }
}

