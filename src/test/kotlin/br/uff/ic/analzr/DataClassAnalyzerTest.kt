package br.uff.ic.analzr

import br.uff.ic.mining.Rule
import org.junit.Assert
import org.junit.Test

class DataClassAnalyzerTest {

    val projectRoot: String = "C:\\Users\\917001\\ideaprojects\\importmining\\src\\test\\kotlin"

    @Test
    fun pureDataClassTest() {
        Assert.assertTrue(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.PureDataClass"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }

    @Test
    fun dataClassWithEqualsTest() {
        Assert.assertTrue(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.DataClassWithEquals"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }

    @Test
    fun dataClassWithToStringTest() {
        Assert.assertTrue(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.DataClassWithToString"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }

    @Test
    fun dataClassWithPropertyChangeListenerTest() {
        Assert.assertTrue(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.DataClassWithPropertyChangeListener"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }

    @Test
    fun normalClassTest() {
        Assert.assertFalse(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.NormalClass"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }

    @Test
    fun dependencyAsCollectionTest() {
        Assert.assertTrue(DataClassAnalyzer().analyze(Rule(listOf("br.uff.ic.analzr.testclasses.NormalClass"), listOf("br.uff.ic.analzr.testclasses.PureDataClass"), 1.0, 1.0, 1.0, 1.0, listOf()), projectRoot) != null)
    }
}