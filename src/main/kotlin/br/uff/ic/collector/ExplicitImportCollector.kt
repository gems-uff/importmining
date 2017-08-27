package br.uff.ic.collector

import br.uff.ic.FileImports
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.io.FileInputStream

class ExplicitImportCollector(private val outputChannel: OutputChannel) : ImportCollector {
    private companion object : Logger by LoggerFactory.new(ExplicitImportCollector::class.java.canonicalName)

    suspend override fun collect(project: Project, output: File) {
        info("Collecting imports of ${project.mainPackage}")
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
            debug("Collected ${localImports.size} imports.")
        }
        info("Finished collect of imports, ${localImports.size} imports where collected")
        val importPerFile = fileImports.map {
            it.copy(imports = it.imports.intersect(localImports).toList().filter { it.isNotEmpty() })
        }.filter { it.imports.isNotEmpty() }
        outputChannel.save(project, importPerFile, output)

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