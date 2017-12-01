package br.uff.ic.analzr

import br.uff.ic.mining.Rule
import br.uff.ic.pipelines.Bucket
import br.uff.ic.pipelines.Pipeline

class AnalyzeRulesPipeline(
                            bucket : Bucket
                          ) : Pipeline<Iterable<Rule>, Any>(bucket) {
    override fun execute(input: Iterable<Rule>): Any {
        TODO("implement use of Rules & bucket to find out if a class has a lack of factory")
    }
}