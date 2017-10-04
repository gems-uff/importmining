package br.uff.ic.collector

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.StringBuilder

class ArffChannel(private val output: File) : OutputChannel {
    override fun save(project: Project, imports: List<FileImports>) {
        try {
            val writer = BufferedWriter(FileWriter(output))
            val sortedLocalImports = imports.map { it.imports }.flatten().toSortedSet().sorted()
            val localImportIndex = sortedLocalImports.mapIndexed { index, s -> s to index }.toMap()
            val builder = StringBuilder()
            builder
                .append("@relation ${output.name.split(".")[0]}")
                .append("\n")

            builder
                .append("@attribute name string")
                .append("\n")

            sortedLocalImports.forEach {
                builder
                    .append("@attribute $it NUMERIC")
                    .append("\n")
            }

            builder
                .append("@data")
                .append("\n")
            imports.forEach {
                builder
                    .append("{0 \"${it.file}\"")
                it.imports.forEach{ import ->
                    builder.append(",${localImportIndex[import]!! + 1} 1")
                }
                builder.append("}\n")
            }
            writer.write(builder.toString())
            writer.close()
        } catch (e: Exception) {
        }
    }
}