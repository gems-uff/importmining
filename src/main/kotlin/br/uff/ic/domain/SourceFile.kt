package br.uff.ic.domain

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart
import com.github.javaparser.Providers
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.GenericVisitorAdapter
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream

/**
 * Class responsible for parsing the imports of the given java file, and grouping the relevant information in the appropriate object
 *
 * @param file the class' file
 * @param imports the class' import statements
 * @param packageName the class' package name
 * */
data class SourceFile(private val file: File, private val project: Project) {

    val imports : Set<String>
    val packageName : String
    /**
     * TODO: tornar mais independente de Java
     * */

    init {
        val compilationUnit = JavaParser().parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(file)))
                .result.get()
        val importVisitor = ImportVisitor()
        importVisitor.visit(compilationUnit, null)
        imports = importVisitor.imports
                        .filter { project.isFromThisProject(it) }
                        .toSet()
        val packageVisitor = PackageVisitor()
        packageVisitor.visit(compilationUnit, null)
        packageName = packageVisitor.packageName
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
     * TODO: use immutable collections, parallelize as possible
     * */
    class ImportVisitor(val imports: MutableCollection<String> = mutableListOf()) : VoidVisitorAdapter<Void>() {
        override fun visit(n: ImportDeclaration, arg: Void?) {
            imports.add(n.nameAsString)
        }
    }

    /**
     * implements the detection of the package
     *
     * TODO: change from voidvisitor to generic visitor, to get the import as return values. Create tests before.
     * */
    class PackageVisitor : VoidVisitorAdapter<Void>() {
        var packageName: String = ""
        override fun visit(n: PackageDeclaration, arg: Void?) {
            packageName = n.nameAsString
        }
    }
}