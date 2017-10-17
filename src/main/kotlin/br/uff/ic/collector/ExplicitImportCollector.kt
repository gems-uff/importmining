package br.uff.ic.collector

import br.uff.ic.extensions.javaFiles
import br.uff.ic.extensions.orNull
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.Row
import io.netty.util.internal.ConcurrentSet
import java.io.File
import kotlin.streams.toList


class ExplicitImportCollector : ImportCollector {

    override fun collect(root: String): DataSet {
        val project = File(root)
        val javaFiles = project.javaFiles.filter {
            !it.contains("/test")
        }.parallelStream().map {
            orNull {
                JavaFile.new(it)
            }
        }.filter {
            it != null
        }.toList()

        println("There are ${javaFiles.size} in this project")

        val projectPackages = javaFiles.map {
            it!!.packageName
        }.filter {
            it.isNotEmpty()
        }.toSet()

        val localImports = ConcurrentSet<String>()
        val imports = javaFiles.parallelStream().map {
            val local = it!!.imports.filter { clazz ->
                projectPackages.any {
                    clazz.contains(it)
                }
            }.toSet()
            localImports.addAll(local)
            it.copy(imports = local)
        }.filter {
            it.imports.isNotEmpty()
        }.toList()
        val header = localImports.sorted()
        val rows = imports.map {
            Row(
                it.file.absolutePath,
                it.imports
            )
        }
        return DataSet(header, rows)
    }
}