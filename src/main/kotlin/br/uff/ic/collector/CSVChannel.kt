package br.uff.ic.collector

import br.uff.ic.FileImports
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class CSVChannel(private val fieldSep: String = ",", private val lineSep: String = "\n") : OutputChannel {
    private companion object : Logger by LoggerFactory.new(CSVChannel::class.java.canonicalName)

    override fun save(project: Project, imports: List<FileImports>, output: File) {
        info("Trying to save the CSV on the file '${output.absolutePath}'")
        try {
            val writer = BufferedWriter(FileWriter(output))
            val sortedLocalImports = imports.map { it.imports }.flatten().toSortedSet().sorted()
            val localImportIndex = sortedLocalImports.mapIndexed { index, s -> s to index }.toMap()
            debug("Writing the header of the file.")
            writer.write("name$fieldSep${sortedLocalImports.joinToString(fieldSep)}")
            writer.write(lineSep)

            var lines = 1
            imports.forEach { (file, imports) ->
                debug("Writing line #$lines.")
                lines += 1
                val cleanName = file.substring(project.project.absolutePath.length + 1, file.length - 5).replace("/", ".")
                val imps = Array(sortedLocalImports.size) { "X" }
                imports.forEach {
                    imps[localImportIndex[it]!!] = "T"
                }
                writer.write("$cleanName,${imps.joinToString(",")}")
                writer.write(lineSep)
            }
            info("Finished writing the CSV")
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}