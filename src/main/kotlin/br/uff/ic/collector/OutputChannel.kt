package br.uff.ic.collector

import java.io.File

interface OutputChannel {
    companion object {
        fun new(output: String): OutputChannel {
            return when (output.split(".").last()) {
                "arff" -> ArffChannel(File(output))
                else -> CSVChannel(File(output))
            }
        }
    }

    fun save(project: Project, imports: List<FileImports>)
}