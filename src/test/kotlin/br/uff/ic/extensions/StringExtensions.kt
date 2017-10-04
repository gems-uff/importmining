
import br.uff.ic.extensions.getJavaClassFile
import br.uff.ic.mining.featureselection.AttributeRemover
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.When
import org.amshove.kluent.`should equal`
import org.amshove.kluent.calling
import org.amshove.kluent.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.junit.Test
import java.io.File
import java.util.*

class StringExtensionsTest {
    /*given("a string") {

        val str = "br.uff.ic.mining.ruleextraction.RuleExtractor"

        on("getting the class ") {


            val classFile = str.getClassFile(File("C:\\Users\\917001\\ideaprojects\\importmining\\src\\main\\kotlin"))
            it("should be the class File for the project root and fully quallified class name") {
                `should equal`(File("C:\\Users\\917001\\ideaprojects\\importmining\\src\\main\\kotlin\\br\\uff\\ic\\mining\\ruleextraction\\RuleExtractor.java"))
            }
        }
    }*/

    @Test
    fun testStringExtension(){
        val str = "br.uff.ic.mining.ruleextraction.RuleExtractor"
        val classFile = str.getJavaClassFile("C:\\Users\\917001\\ideaprojects\\importmining\\src\\main\\kotlin")

        Assert.assertEquals(classFile, File("C:\\Users\\917001\\ideaprojects\\importmining\\src\\main\\kotlin\\br\\uff\\ic\\mining\\ruleextraction\\RuleExtractor.java"))
    }
}