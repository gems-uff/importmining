package br.uff.ic.collector

import java.io.File

interface OutputChannel {
    fun save(project: Project, imports: List<FileImports>, output: File)
}