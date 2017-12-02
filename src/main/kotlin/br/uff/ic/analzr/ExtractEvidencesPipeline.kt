package br.uff.ic.analzr

import br.uff.ic.extensions.load
import br.uff.ic.mining.Rule
import br.uff.ic.pipelines.Bucket
import br.uff.ic.pipelines.Pipeline

class ExtractEvidencesPipeline(
        bucket : Bucket
) : Pipeline<Iterable<Rule>, Iterable<Evidence>>(bucket) {

    override fun execute(input: Iterable<Rule>): Iterable<Evidence> {
        println("Any missing factories?")
        val missingFactoryEvidences = load("missing-factory-evidences"){
            // TODO: get project root in here
            MissingFactoryAnalyzer().analyze(input, "")
        }
        println("${missingFactoryEvidences.count()} possible cases.")

        println("Any data classes?")
        val dataClassEvidences = load("data-class-evidences"){
            input.mapNotNull { DataClassAnalyzer().analyze(it, "") }
        }
        println("${dataClassEvidences.count()} possible cases.")

        return missingFactoryEvidences.union(dataClassEvidences)
    }
}