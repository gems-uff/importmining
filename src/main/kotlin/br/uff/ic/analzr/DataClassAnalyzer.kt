package br.uff.ic.analzr

import br.uff.ic.extensions.getJavaClassFile
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.Rule
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.VoidType
import com.github.javaparser.ast.type.Type
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.FileInputStream

class DataClassAnalyzer : Analyzer<Rule, Evidence> {
    private companion object : Logger by LoggerFactory.new(DataClassAnalyzer::class.java)

    /**
     *  Analyzes the rule for basis on a data class, based on the ocurrence of the following rule:
     *
     *
     * A -> B, where A or B depend on each other and the depended type only has getters & setters
     */
    override fun analyze(basis: Rule, projectRoot: String): Evidence? {
        info("Analyzing rule for Data Classes")

        var compilationUnit : CompilationUnit
        val candidates = mutableListOf<String>()
        val candidateFields = mutableListOf<Type>()

        candidates.addAll(basis.premises)

        basis.premises.forEach {
            debug("premise: $it")
            compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))

            FieldVisitor(candidateFields).visit(compilationUnit, null)

            MethodVisitor(candidates, it, candidateFields).visit(compilationUnit, null)
        }

        candidates.addAll(basis.consequent)

        basis.consequent.forEach {
            debug("consequent: $it")

            compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))

            FieldVisitor(candidateFields).visit(compilationUnit, null)

            MethodVisitor(candidates, it, candidateFields).visit(compilationUnit, null)
        }

        // as of now candidates should hold the classes that don't have any method other than getters or setters.
        return if(candidates.isNotEmpty()) Evidence("Data Class", listOf(basis), candidates) else null
    }
}

private class MethodVisitor(val candidates: MutableCollection<String>,
                            val currentClass: String,
                            val classFields: MutableCollection<Type>) : VoidVisitorAdapter<Void>() {

    override fun visit(md: MethodDeclaration, arg: Void?) {
        // as a strategy to verify if a method is getter or setter,
        // this will make various assertions on the method's characteristics, (on the most obvious ones first)
        // until there's enough guarantee that the method is a getter or setter

        if(md.parameters.size > 1){
            candidates.remove(currentClass)
            return
        }

        when(md.nameAsString){
            "equals", "toString", "hashCode" -> return
        }

        if(md.nameAsString.contains("PropertyChangeListener")) return

        if(VoidType() == md.type){
            // let's assume it's a setter

            md.parameters.map{it.type}.forEach {
                // parameter does not have the type of any field

                if(!classFields.contains(it)){
                    candidates.remove(currentClass)
                    return
                }
            }

        } else {
            // let's assume it's a getter

            if(!classFields.contains(md.type)){
                candidates.remove(currentClass)
                return
            }

        }
    }
}

private class FieldVisitor(val parameters : MutableCollection<Type>) : VoidVisitorAdapter<Void>() {

    override  fun visit(fd: FieldDeclaration, arg: Void?){
        parameters += fd.elementType
    }
}