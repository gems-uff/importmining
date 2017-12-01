package br.uff.ic.analzr

import br.uff.ic.extensions.getJavaClassFile
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.Rule
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.GenericVisitorAdapter
import java.io.FileInputStream
import java.io.IOException


class MissingFactoryAnalyzer : Analyzer<Iterable<Rule>>{
    private companion object : Logger by LoggerFactory.new(MissingFactoryAnalyzer::class.java.canonicalName)

    /**
     *  Analyzes the project's generated rules for evidence on a missing factory, based on the ocurrence of the following rule:
     *
     *
     * A -> B, where A is an interface and B is a class implementing this interface
     */
    override fun analyze(evidence : Iterable<Rule>, projectRoot: String): Boolean {

        info("Analyzing rule for missing factories")

        val classes = mutableListOf<String>()
        val trackedEvidence = evidence.map { UsedRuleTracker(evidence.indexOf(it), it,null) }
                                      .sortedBy { it.id }

        val classesToScan = trackedEvidence.flatMap { it.r.items }
                                           .distinct()
                                           .associateBy({ it }, {r -> trackedEvidence.filter { it.r.items.contains(r) }
                                                                                     .map{it.id}})

        val implementedTypes = mutableListOf<String>()

        try {
            classesToScan.forEach{
                it.value.forEach {

                    if(trackedEvidence[it].hasImplementingInformation == null){
                        trackedEvidence[it].r.premises.forEach {
                            if(AbstractClassInterfaceVisitor()
                                    .visit(JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot))), null))
                                implementedTypes.add(it)
                        }

                        if(implementedTypes.size > 0) {
                            trackedEvidence[it].hasImplementingInformation = trackedEvidence[it].r.consequent.any {

                                return ImplementingClassVisitor(implementedTypes, classes)
                                            .visit(JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot))), null)
                            }
                        }
                    }
                }
            }


        } catch (e: IOException) {
            error(e.message!!.substringAfter(':', e.message!!))
        }

        return classes.isNotEmpty()
    }
}

private class ImplementingClassVisitor(val implementedTypes: MutableCollection<String>,
                                       val classes: MutableCollection<String>) : GenericVisitorAdapter<Boolean, Void>() {
    override fun visit(n: ClassOrInterfaceDeclaration, arg: Void?) : Boolean =

        !n.isInterface && !n.isAbstract
                       && n.implementedTypes.map { it.nameAsString }
                                            .intersect(implementedTypes)
                                            .isNotEmpty()
                       && classes.add(n.nameAsString)

}

private class AbstractClassInterfaceVisitor : GenericVisitorAdapter<Boolean, Void>() {
    override fun visit(n: ClassOrInterfaceDeclaration, arg: Void?) : Boolean = n.isInterface || n.isAbstract
}

// Surrogate class, attempt to reutilize previous processing, yet to be tested and profiled
private data class UsedRuleTracker(val id : Int, val r : Rule, var hasImplementingInformation : Boolean?)