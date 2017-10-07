package br.uff.ic.pipelines

import br.uff.ic.collector.ImportCollector
import br.uff.ic.mining.Knowledge
import br.uff.ic.mining.RuleExtractor
import br.uff.ic.vcs.VCS
import kotlinx.coroutines.experimental.runBlocking
import java.util.*


class ExtractRulesPipeline(
        private val vcs: VCS,
        private val collector: ImportCollector,
        private val extractor: RuleExtractor,
        checkpoints: Bucket
) : Pipeline<String, Knowledge>(
        checkpoints
) {
    override fun execute(input: String): Knowledge {
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

fun Any.toBase64(): String {
    return Base64.getEncoder().encodeToString(this.hashCode().toString().toByteArray())
}

