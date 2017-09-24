package br.uff.ic.importmining.mining.ruleextractor

import br.uff.ic.mining.ruleextraction.RuleExtractor
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should throw`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object RuleExtractorSpec : Spek({
    given("the RuleExtractor") {
        context("trying to create the FPGrowth") {
            on("calling #new with a valid specification") {
                val spec = mapOf<Any, Any>(
                        "workers" to 10,
                        "FPGrowth" to mapOf<Any, Any>(
                                "support" to mapOf<Any, Any>(
                                        "minimum" to 0.1,
                                        "maximum" to 1,
                                        "step" to 0.01
                                ),
                                "targetMetric" to mapOf<Any, Any>(
                                        "name" to "confidence",
                                        "minimum" to 0.1
                                ),
                                "numberOfRulesToFind" to 10
                        )
                )
                val extractor = RuleExtractor.new(spec)
                it("should return a MultipleAlgorithmRuleExtractor with exact one algorithm") {
                    extractor `should be instance of` MultipleAlgorithmRuleExtractor::class
                    extractor as MultipleAlgorithmRuleExtractor
                    extractor.numberOfAlgorithms `should equal` 1
                }
            }
            on("calling #new with an invalid specification") {
                val spec = mapOf<Any, Any>(
                        "FPGrowth" to mapOf<Any, Any>(
                                "support" to mapOf<Any, Any>(
                                        "minimum" to "a",
                                        "maximum" to 1,
                                        "step" to 0.01
                                ),
                                "targetMetric" to mapOf<Any, Any>(
                                        "name" to "confidence",
                                        "minimum" to 0.1
                                ),
                                "numberOfRulesToFind" to 10
                        )
                )
                val new = { RuleExtractor.new(spec) }
                it("should throw an exception") {
                    new `should throw` RuntimeException::class
                }
            }
        }
        context("trying to create a non-existing RuleExtractor") {
            on("calling #new with an invalid specification") {
                val spec = mapOf<Any, Any>(
                        "Ruler" to mapOf<Any, Any>(
                                "support" to mapOf<Any, Any>(
                                        "minimum" to "a",
                                        "maximum" to 1,
                                        "step" to 0.01
                                ),
                                "targetMetric" to mapOf<Any, Any>(
                                        "name" to "confidence",
                                        "minimum" to 0.1
                                ),
                                "numberOfRulesToFind" to 10
                        )
                )
                val new = { RuleExtractor.new(spec) }
                it("should throw an exception") {
                    new `should throw` RuntimeException::class
                }
            }

        }
    }
})