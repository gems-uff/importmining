package br.uff.ic.extensions

import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File

fun File.listFilesRecursively(predicate : (File) -> Boolean): List<String> {
    val filteredFiles = this.listFiles { file -> predicate(file) }
                            .map { it.absolutePath }

    val childrenFilteredFiles = this.listFiles { file -> file.isDirectory }
            .flatMap { it.listFilesRecursively(predicate) }
    return filteredFiles + childrenFilteredFiles
}

private class PackageVisitor : VoidVisitorAdapter<Void>() {
    var packageName: String = ""
    override fun visit(n: PackageDeclaration, arg: Void?) {
        packageName = n.nameAsString
        super.visit(n, arg)
    }
}

fun File.createIfNotExists() : File{
    if(!this.exists()) this.mkdirs()
    return this
}