package br.uff.ic.extensions

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

fun File.deleteOnShutdown() {
    Runtime.getRuntime().addShutdownHook(Thread {
        fun delete(f: File) {
            if (f.isDirectory) {
                for (c in f.listFiles())
                    delete(c)
            }
            if (!f.delete())
                throw FileNotFoundException("Failed to delete file: " + f)
        }
        delete(this.absoluteFile)
    })
}

fun File.listFileWithSuffix(suffix: String): List<String> {
    val filteredFiles = this.listFiles { file ->
        file.name.endsWith(suffix)
    }.map { it.absolutePath }.toList()
    val childrenFilteredFiles = this.listFiles { file ->
        file.isDirectory
    }.map { it.listFileWithSuffix(suffix) }.flatten()
    return childrenFilteredFiles + filteredFiles
}

private class PackageVisitor : VoidVisitorAdapter<Void>() {
    var packageName: String = ""
    override fun visit(n: PackageDeclaration, arg: Void?) {
        packageName = n.nameAsString
        super.visit(n, arg)
    }
}


val File.mainPackage: String
    get() {
        val notAPackage = this.absolutePath.replace("\\", ".")
        return this.javaFiles.parallelStream().map {
            try {
                val v = PackageVisitor()
                val input = FileInputStream(it)
                val cu = JavaParser.parse(input)
                v.visit(cu, null)
                v.packageName
            } catch (e: Exception) {
                println("File -> ${this.absolutePath} deu erro")
                ""
            }
        }.filter {
            it.isNotEmpty() && notAPackage.contains(it)
        }.min { a, b ->
            a.compareTo(b)
        }.orElse("")
    }

val File.javaFiles: Set<String>
    get () = listFileWithSuffix(".java").toSet()