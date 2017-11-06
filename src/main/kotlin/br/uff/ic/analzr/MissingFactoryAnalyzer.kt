package br.uff.ic.analzr

import br.uff.ic.extensions.getJavaClassFile
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.Rule
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.FileInputStream
import java.io.IOException


class MissingFactoryAnalyzer : Analyzer{
    private companion object : Logger by LoggerFactory.new(MissingFactoryAnalyzer::class.java.canonicalName)

    /**
     *  Analyzes the project for evidence on a missing factory, based on the ocurrence of the following rule:
     *
     *
     * A -> B, where A is an interface and B is a class implementing this interface
     */
    override fun analyze(r: Rule, projectRoot: String): Boolean {

        info("Analyzing rule for missing factories")

        var compilationUnit: CompilationUnit
        val candidates = mutableListOf<String>()
        val classes = mutableListOf<String>()

        try {
            r.premises.forEach {
                debug("premise: $it")
                compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))
                InterfaceVisitor(candidates).visit(compilationUnit, null)
            }

            r.consequent.forEach {
                debug("consequence: $it")
                compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))
                ClassVisitor(classes, candidates).visit(compilationUnit, null)
            }
        } catch (e: IOException) {
            error(e.message!!.substringAfter(':', e.message!!))
        }

        return classes.isNotEmpty()
    }
}

private class InterfaceVisitor(val candidates: MutableCollection<String>) : VoidVisitorAdapter<Void>() {
    override fun visit(n: ClassOrInterfaceDeclaration, arg: Void?) {
        if (n.isInterface) candidates.add(n.nameAsString)
    }
}

private class ClassVisitor(val classes: MutableCollection<String>,
                           val interfaces: MutableCollection<String>) : VoidVisitorAdapter<Void>() {
    override fun visit(n: ClassOrInterfaceDeclaration, arg: Void?) {
        if (!n.isInterface && n.implementedTypes.map { it.nameAsString }.intersect(interfaces).isNotEmpty()) classes.add(n.nameAsString)
    }
}