package br.uff.ic.collector

import br.uff.ic.FileImports
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class CSVChannel(private val fieldSep: String = ",", private val lineSep: String = "\n") : OutputChannel {
    override fun save(imports: Iterable<FileImports>, output: File) {
        try {
            val writer = BufferedWriter(FileWriter(output))
            val sortedLocalImports = imports.map { it.file }.sorted()
            val localImportIndex = sortedLocalImports.mapIndexed { index, s -> s to index }.toMap()
            writer.write("name$fieldSep${sortedLocalImports.joinToString(fieldSep)}")
            writer.write(lineSep)
            imports.forEach { (file, imports) ->
                val cleanName = file.substring(106, file.length - 5).replace("/", ".")
                val imps = Array(sortedLocalImports.size) { "X" }
                imports.forEach {
                    imps[localImportIndex[it]!!] = "T"
                }
                writer.write("$cleanName,${imps.joinToString(",")}")
                writer.write(lineSep)
            }
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}