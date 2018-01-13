package br.uff.ic.analzr

import br.uff.ic.mining.Rule
import org.amshove.kluent.`should not be empty`
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.Assert
import org.junit.Test


class MissingFactoryAnalyzerTest {

    val projectRoot: String = "C:\\Users\\917001\\Desktop\\temp\\pspa-gcp\\src\\main\\java"

    @Test
    fun shouldBeTrue() {
        Assert.assertTrue(MissingFactoryAnalyzer().analyze(listOf(Rule(listOf("org.pspa.gcp.visao.adaptadores.Adaptador"), listOf("org.pspa.gcp.visao.adaptadores.EntradaBooleanos", "org.pspa.gcp.visao.adaptadores.EntradaDatas"), 1.0, 1.0, 1.0, 1.0, listOf())), projectRoot).count() > 0)
    }

    @Test
    fun shouldBeFalse() {
        Assert.assertFalse(MissingFactoryAnalyzer().analyze(listOf(Rule(listOf("org.pspa.gcp.visao.adaptadores.Adaptador"), listOf("javafx.scene.Node", "javafx.scene.layout.GridPane"), 1.0, 1.0, 1.0, 1.0, listOf())), projectRoot).count() > 0)
    }
}