package br.uff.ic.collector

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart
import com.github.javaparser.Providers
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream

data class JavaFile(val file: File, val imports: Set<String>, val packageName: String) {
    companion object {
        fun new(file: File): JavaFile {
            val cu = JavaParser().parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(file))).result.get()
            val imports = mutableSetOf<String>()
            ImportVisitor(imports).visit(cu, null)
            val packageVisitor = PackageVisitor()
            packageVisitor.visit(cu, null)
            return JavaFile(file, imports, packageVisitor.packageName)
        }

        fun new(file: String): JavaFile {
            return new(File(file))
        }
    }

    private class ImportVisitor(val imports: MutableCollection<String>) : VoidVisitorAdapter<Void>() {
        override fun visit(n: ImportDeclaration, arg: Void?) {
            imports.add(n.nameAsString)
        }
    }

    private class PackageVisitor : VoidVisitorAdapter<Void>() {
        var packageName: String = ""
        override fun visit(n: PackageDeclaration, arg: Void?) {
            packageName = n.nameAsString
        }
    }
}