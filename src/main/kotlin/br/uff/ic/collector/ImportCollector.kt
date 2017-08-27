package br.uff.ic.collector

import java.io.File

interface ImportCollector {
    suspend fun collect(project: Project, output: File)
}