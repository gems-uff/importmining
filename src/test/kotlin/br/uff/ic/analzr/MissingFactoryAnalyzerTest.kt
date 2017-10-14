package br.uff.ic.analzr

import br.uff.ic.mining.ruleextraction.Rule
import org.amshove.kluent.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class MissingFactoryAnalyzerTest{

    val projectRoot: String = "C:\\Users\\917001\\Desktop\\temp\\pspa-gcp\\src\\main\\java"

    @Test
    fun shouldBeTrue(){
        Assert.assertTrue(MissingFactoryAnalyzer().analyze(Rule(listOf("org.pspa.gcp.visao.adaptadores.Adaptador"), listOf("org.pspa.gcp.visao.adaptadores.EntradaBooleanos", "org.pspa.gcp.visao.adaptadores.EntradaDatas"), 1.0, 1.0, 1.0, 1.0, 1.0), projectRoot))
    }

    @Test
    fun shouldBeFalse(){
        Assert.assertFalse(MissingFactoryAnalyzer().analyze(Rule(listOf("org.pspa.gcp.visao.adaptadores.Adaptador"), listOf("javafx.scene.Node", "javafx.scene.layout.GridPane"), 1.0, 1.0, 1.0, 1.0, 1.0), projectRoot))
    }
}