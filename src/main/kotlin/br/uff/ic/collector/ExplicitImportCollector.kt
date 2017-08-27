package br.uff.ic.collector

import br.uff.ic.FileImports
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.io.FileInputStream

class ExplicitImportCollector(private val outputChannel: OutputChannel) : ImportCollector {
    suspend override fun collect(project: Project, output: File) {
        val fileImportsChannel = Channel<FileImports>()
        project.javaFiles.forEach {
            collect(it, fileImportsChannel)
        }
        val fileImports = mutableListOf<FileImports>()
        val localImports = mutableSetOf<String>()
        (1..project.javaFiles.size).forEach {
            val imp = fileImportsChannel.receive()
            fileImports.add(imp)
            localImports.addAll(imp.imports.filter { it.startsWith(project.mainPackage) })
        }
        val importPerFile = fileImports.map {
            it.copy(imports = it.imports.intersect(localImports).toList())
        }.filter { it.imports.isNotEmpty() }
        outputChannel.save(importPerFile, output)

    }

    private fun collect(file: String, output: Channel<FileImports>) {
        launch(CommonPool) {
            val input = FileInputStream(file)
            val cu = JavaParser.parse(input)
            val fileImports = mutableListOf<String>()
            ImportVisitor(fileImports).visit(cu, null)
            output.send(FileImports(file, fileImports))
        }
    }

    private class ImportVisitor(val imports: MutableCollection<String>) : VoidVisitorAdapter<Void>() {
        override fun visit(n: ImportDeclaration, arg: Void?) {
            imports.add(n.nameAsString)
        }
    }
}