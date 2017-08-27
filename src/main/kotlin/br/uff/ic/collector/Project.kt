package br.uff.ic.collector

import br.uff.ic.io.listFileWithSuffix
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream

class Project(project: File) {
    val javaFiles: List<String> = project.listFileWithSuffix(".java")
    val mainPackage = project.listFiles { file ->
        file.name.endsWith(".java")
    }.first().let {
        val v = PackageVisitor()
        val input = FileInputStream(it)
        val cu = JavaParser.parse(input)
        v.visit(cu, null)
        v.packageName
    }

    private class PackageVisitor : VoidVisitorAdapter<Void>() {
        var packageName: String = ""
        override fun visit(n: PackageDeclaration, arg: Void?) {
            packageName = n.nameAsString
            super.visit(n, arg)
        }
    }
}