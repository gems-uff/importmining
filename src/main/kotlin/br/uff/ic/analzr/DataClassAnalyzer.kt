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

class DataClassAnalyzer : Analyzer {
    private companion object : Logger by LoggerFactory.new(DataClassAnalyzer::class.java.canonicalName)

    /**
     *  Analyzes the rule for evidence on a data class, based on the ocurrence of the following rule:
     *
     *
     * A -> B, where A or B depend on each other and the depended type only has getters & setters
     */
    override fun analyze(evidence: Rule, projectRoot: String): Boolean {
        info("Analyzing rule for Data Classes")

        var compilationUnit : CompilationUnit
        val candidates = mutableListOf<String>()
        var candidateFields = mutableListOf<Type>()

        candidates.addAll(evidence.premises)

        evidence.premises.forEach {
            debug("premise: $it")
            compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))

            FieldVisitor(candidateFields).visit(compilationUnit, null)

            MethodVisitor(candidates, it, candidateFields).visit(compilationUnit, null)
        }

        candidates.addAll(evidence.consequent)

        evidence.consequent.forEach {
            debug("consequent: $it")

            compilationUnit = JavaParser.parse(FileInputStream(it.getJavaClassFile(projectRoot)))

            FieldVisitor(candidateFields).visit(compilationUnit, null)

            MethodVisitor(candidates, it, candidateFields).visit(compilationUnit, null)
        }

        // as of now candidates should hold the classes that don't have any method other than getters or setters.
        return candidates.isNotEmpty()
    }
}

private class MethodVisitor(val candidates: MutableCollection<String>,
                            val currentClass: String,
                            val classFields: MutableCollection<Type>) : VoidVisitorAdapter<Void>() {

    override fun visit(md: MethodDeclaration, arg: Void?) {
        // as a strategy to verify if a method is getter or setter,
        // this will make various assertions on the caracteristics of the method, (on the most obvious ones first)
        // until theres absolute guarantee that the method is a getter or setter

        if(md.parameters.size > 1){
            candidates.remove(currentClass)
            return
        }

        if(VoidType() == md.type){
            // let's assume it's a setter

            md.parameters.map { it.type }.forEach {
                // parameter does not have the type of any field

                if(!classFields.contains(it)){
                candidates.remove(currentClass)
                return
            }
            }

            // TODO: verify the expression to see if it has an atribution

        } else {
            // let's assume it's a getter

            if(!classFields.contains(md.type)){
                candidates.remove(currentClass)
                return
            }

            // TODO: verify the expression to see if it has a return of a field
        }
    }
}

private class FieldVisitor(val parameters : MutableCollection<Type>) : VoidVisitorAdapter<Void>() {

    override  fun visit(fd: FieldDeclaration, arg: Void?){
        parameters += fd.elementType
    }
}