package br.uff.ic.importmining.mining.featureselection

import br.uff.ic.mining.featureselection.FeatureSelector
import br.uff.ic.mining.featureselection.PipelineFeatureSelector
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should throw`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object FeatureSelectorSpec : Spek({
    given("the FeatureSelector") {
        context("trying to create the AttributeRemover") {
            on("calling #new with a valid specification") {
                val spec = listOf<Any>(
                        mapOf<Any, Any>(
                                "AttributeRemover" to listOf(
                                        "attr0",
                                        "attr1",
                                        "attr2"
                                )
                        )
                )
                val selector = FeatureSelector.new(spec)
                it("should return a PipelineFeatureSelector with exact one step") {
                    selector `should be instance of` PipelineFeatureSelector::class
                    selector as PipelineFeatureSelector
                    selector.numberOfSteps `should equal` 1
                }
            }
            on("calling #new with a invalid specification") {
                val spec = listOf<Any>(
                        mapOf<Any, Any>(
                                "AttributeRemover" to listOf(
                                        1,
                                        2,
                                        3
                                )
                        )
                )
                val new = { FeatureSelector.new(spec) }
                it("should throw a Exception") {
                    new `should throw` RuntimeException::class
                }
            }
        }
        context("trying to create the EveryOtherAttributeRemover") {
            on("calling #new with a valid specification") {
                val spec = listOf<Any>(
                        mapOf<Any, Any>(
                                "EveryOtherAttributeRemover" to listOf(
                                        "attr0",
                                        "attr1",
                                        "attr2"
                                )
                        )
                )
                val selector = FeatureSelector.new(spec)
                it("should return a PipelineFeatureSelector with exact one step") {
                    selector `should be instance of` PipelineFeatureSelector::class
                    selector as PipelineFeatureSelector
                    selector.numberOfSteps `should equal` 1
                }
            }
            on("calling #new with a invalid specification") {
                val spec = listOf<Any>(
                        mapOf<Any, Any>(
                                "EveryOtherAttributeRemover" to listOf(
                                        1,
                                        2,
                                        3
                                )
                        )
                )
                val new = { FeatureSelector.new(spec) }
                it("should throw a Exception") {
                    new `should throw` RuntimeException::class
                }
            }
        }
        context("trying to create a non-existing FeatureSelector") {
            on("calling #new with a invalid specification") {
                val spec = listOf<Any>(
                        mapOf<Any, Any>(
                                "NonExistingFeatureSelector" to ""
                        )
                )
                val new = { FeatureSelector.new(spec) }
                it("should throw a Exception") {
                    new `should throw` RuntimeException::class
                }
            }
        }
    }
})