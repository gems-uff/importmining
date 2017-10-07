package br.uff.ic.collector

import br.uff.ic.extensions.javaFiles
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.Row
import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart.COMPILATION_UNIT
import com.github.javaparser.Providers
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import io.netty.util.internal.ConcurrentSet
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.io.File
import java.io.FileInputStream

class ExplicitImportCollector : ImportCollector {
    private companion object : Logger by LoggerFactory.new(ExplicitImportCollector::class.java.canonicalName)

    override fun collect(root: String): DataSet {
        info("Collecting imports of $root")
        val fileImportsChannel = Channel<FileImports>()
        val project = File(root)
        val javaFiles = project.javaFiles.filter {
            !it.contains("/test")
        }
        javaFiles.forEach {
            collect(it, fileImportsChannel)
        }
        val fileImports = mutableListOf<FileImports>()
        val localImports = ConcurrentSet<String>()
        runBlocking {
            (1..javaFiles.size).forEach {
                val imp = fileImportsChannel.receive()
                fileImports.add(imp)
            }
        }
        val mainPackage = mainPackageOf(javaFiles)
        fileImports.parallelStream().forEach { imp ->
            localImports.addAll(imp.imports.filter { it.startsWith(mainPackage) })
            debug("Collected ${localImports.size} imports.")
        }
        info("Finished collect of imports, ${localImports.size} imports where collected")
        val sortedLocalImports = localImports.sorted()
        val importPerFile = fileImports.map {
            Row(
                it.file,
                it.imports.filter {
                    sortedLocalImports.contains(it)
                }.toSet()
            )
        }.filter {
            it.set.isNotEmpty()
        }
        return DataSet(sortedLocalImports, importPerFile)
    }

    private fun mainPackageOf(files: List<String>): String {
        val target = files.minBy { file ->
            file.split("/").size
        }
        try {
            val input = FileInputStream(target)
            val cu = JavaParser().parse(COMPILATION_UNIT, Providers.provider(input)).result.get()
            val packageVisitor = PackageVisitor()
            packageVisitor.visit(cu, null)
            return packageVisitor.packageName
        } catch (e: Exception) {
            error("File: $target -> ${e.message}")
            e.printStackTrace()
            return ""
        }
    }
}


private class PackageVisitor : VoidVisitorAdapter<Void>() {
    var packageName: String = ""
    override fun visit(n: PackageDeclaration, arg: Void?) {
        packageName = n.nameAsString
        super.visit(n, arg)
    }
}


private fun collect(file: String, output: Channel<FileImports>) {
    launch(CommonPool) {
        try {
            val input = FileInputStream(file)
            val cu = JavaParser().parse(COMPILATION_UNIT, Providers.provider(input)).result.get()
            val fileImports = mutableListOf<String>()
            ImportVisitor(fileImports).visit(cu, null)
            output.send(FileImports(file, fileImports))
        } catch (e: Exception) {
            error("File: $file -> ${e.message}")
            e.printStackTrace()
            output.send(FileImports("", setOf()))
        }
    }
}

private class ImportVisitor(val imports: MutableCollection<String>) : VoidVisitorAdapter<Void>() {
    override fun visit(n: ImportDeclaration, arg: Void?) {
        imports.add(n.nameAsString)
    }
}
