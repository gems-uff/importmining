package br.uff.ic.importmining.mining.featureselection

import br.uff.ic.mining.featureselection.Filter
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object PipelineFeatureSelectorSpec : Spek({
    given("a PipelineFeatureSelector") {
        on("calling #select") {
            val newSelector = {
                val selector = mock<Filter>()
                When calling selector.select(any()) `it returns` mock()
                selector
            }
            val selectors = arrayOf(
                    newSelector(),
                    newSelector(),
                    newSelector()
            )

            val pipeline = PipelineFeatureSelector(*selectors)
            pipeline.select(mock())
            it("should call the method 'select' of all the sub-selectors") {
                selectors.forEach { verify(it).select(any()) }
            }
        }
    }
})