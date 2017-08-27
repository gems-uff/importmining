package br.uff.ic.collector

import br.uff.ic.io.listFileWithSuffix
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream

class Project(val project: File) {
    val javaFiles: List<String> = project.listFileWithSuffix(".java")
    val mainPackage = javaFiles.map {
        val v = PackageVisitor()
        val input = FileInputStream(it)
        val cu = JavaParser.parse(input)
        v.visit(cu, null)
        v.packageName
    }.filter { it.isNotEmpty() }.min()!!

    private class PackageVisitor : VoidVisitorAdapter<Void>() {
        var packageName: String = ""
        override fun visit(n: PackageDeclaration, arg: Void?) {
            packageName = n.nameAsString
            super.visit(n, arg)
        }
    }
}