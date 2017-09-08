package br.uff.ic.importmining.mining.featureselection

import br.uff.ic.mining.featureselection.AttributeRemover
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

object AttributeRemoverSpec : Spek({
    given("an AttributeRemover") {
        on("calling #select") {
            val remover = AttributeRemover(
                    "attr0",
                    "attr1"
            )
            val dataSet = mock<Instances>()
            val attr0 = Attribute("attr0", 0)
            val attr1 = Attribute("attr1", 1)
            val attributes = Vector(setOf(attr0, attr1)).elements()
            When calling dataSet.enumerateAttributes() `it returns` attributes
            remover.select(dataSet)
            it("should call the method 'deleteAttributeAt'of the dataSet") {
                verify(dataSet).deleteAttributeAt(eq(0))
                verify(dataSet).deleteAttributeAt(eq(1))
            }
        }
    }
})