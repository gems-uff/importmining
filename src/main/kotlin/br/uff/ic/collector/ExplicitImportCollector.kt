package br.uff.ic.collector

import br.uff.ic.extensions.javaFiles
import br.uff.ic.extensions.mainPackage
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.featureselection.DataSet
import com.github.javaparser.JavaParser
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.io.FileInputStream

class ExplicitImportCollector: ImportCollector {
    private companion object : Logger by LoggerFactory.new(ExplicitImportCollector::class.java.canonicalName)

    suspend override fun collect(root: String): DataSet {
        info("Collecting imports of $root")
        val fileImportsChannel = Channel<FileImports>()
        val project = File(root)
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
        val sortedLocalImports = localImports.sorted()
        val importPerFile = fileImports.map {
            it.file to it.imports.filter {
                sortedLocalImports.contains(it)
            }.map {
                sortedLocalImports.indexOf(it)
            }
        }
        return DataSet(sortedLocalImports, importPerFile)

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