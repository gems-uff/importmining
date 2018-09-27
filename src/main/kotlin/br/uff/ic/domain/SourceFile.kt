package br.uff.ic.domain

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart
import com.github.javaparser.Providers
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream

/**
 * Class responsible for parsing the imports of the given java file, and grouping the relevant information in the appropriate object
 *
 * @param file the class' file
 * @param imports the class' import statements
 * */
data class SourceFile(private val file: File, private val _imports : List<String> = listOf()) {

    val packageName : String
    val imports : List<String>

    init {
        val compilationUnit = JavaParser().parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(file)))
                .result.get()

        packageName = PackageVisitor().let {
            it.visit(compilationUnit, null)
            it.packageName
        }
        imports = if(_imports.isEmpty()){
             ImportVisitor().let {
             it.visit(compilationUnit, null)
             it.imports.toList()
            }
        } else _imports

    }

    fun removeExternalImports(project: Project) : SourceFile {
        return SourceFile(file, this.imports.filter { project.defines(it) })
    }

    /**
     * assumptions:
     * called from whole constructor does not execute javaparser
     * called from second constructor works with javaparser
     * imports are all from this project
     * imports are not empty (for a nonempty importlist class)
     * package is the . separated package name
     * */

    fun getFilePath() : String = file.absolutePath

    /**
     * implements the collection of imports
     *
     * */
    class ImportVisitor(val imports: MutableCollection<String> = mutableListOf()) : VoidVisitorAdapter<Void>() {
        override fun visit(n: ImportDeclaration, arg: Void?) {
            imports.add(n.nameAsString)
        }
    }

    /**
     * implements the detection of the package
     *
     * */
    class PackageVisitor : VoidVisitorAdapter<Void>() {
        var packageName: String = ""
        override fun visit(n: PackageDeclaration, arg: Void?) {
            packageName = n.nameAsString
        }
    }

}