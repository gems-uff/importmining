package br.uff.ic.importmining.mining.featureselection

import br.uff.ic.mining.featureselection.EveryOtherAttributeRemover
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.When
import org.amshove.kluent.`it returns`
import org.amshove.kluent.calling
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import weka.core.Attribute
import weka.core.Instances
import java.util.*

object EveryOtherAttributeRemoverSpec : Spek({
    given("an EveryOtherAttributeRemover") {
        on("calling #select") {
            val remover = EveryOtherAttributeRemover(
                    "attr0",
                    "attr2"
            )
            val dataSet = mock<Instances>()
            val attr0 = Attribute("attr0", 0)
            val attr1 = Attribute("attr1", 1)
            val attributes = Vector(setOf(attr0, attr1)).elements()
            When calling dataSet.enumerateAttributes() `it returns` attributes
            remover.select(dataSet)
            it("should call the method 'deleteAttributeAt'of the dataSet") {
                verify(dataSet).deleteAttributeAt(eq(1))
            }
        }
    }
})