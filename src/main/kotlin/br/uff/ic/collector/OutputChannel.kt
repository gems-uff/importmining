package br.uff.ic.collector

import br.uff.ic.FileImports
import java.io.File

interface OutputChannel {
    fun save(project: Project, imports: List<FileImports>, output: File)
}