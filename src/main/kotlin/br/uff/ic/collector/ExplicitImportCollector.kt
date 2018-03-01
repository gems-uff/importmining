package br.uff.ic.collector

import br.uff.ic.domain.Project
import br.uff.ic.extensions.orNull
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.Row
import io.netty.util.internal.ConcurrentSet
import kotlin.streams.toList


class ExplicitImportCollector : ImportCollector {

    override fun collect(project : Project): DataSet {

        val javaFiles = project//.sourceFiles
                .sourcePaths
         .parallelStream().map {
            orNull {
                JavaFile.new(it)
            }
        }.filter {
            it != null
        }.toList()
        /*val javaFiles = project.sourceFiles*/

        val projectPackages = javaFiles.map {
            it!!.packageName
        }.filter {
            it.isNotEmpty()
        }.toSet()

        val localImports = ConcurrentSet<String>()
        val srcFiles = javaFiles.parallelStream()
                .map {
            val local = it!!.imports.filter { clazz ->
                projectPackages.any {
                    clazz.contains(it)
                }
            }.toSet()
            localImports.addAll(local)
            it.copy(imports = local)
        }.filter { it.imports.isNotEmpty() }.toList()
        val header = localImports.sorted()
        val rows = srcFiles.map {
            Row(it!!.file.path, it.imports.toSet()  )
        }
        return DataSet(header, rows)
    }
}