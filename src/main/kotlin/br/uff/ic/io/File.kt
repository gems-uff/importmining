package br.uff.ic.io

import java.io.File
import java.io.FileNotFoundException

fun File.deleteOnShutdown() {
    Runtime.getRuntime().addShutdownHook(Thread {
        fun delete(f: File) {
            if (f.isDirectory) {
                for (c in f.listFiles())
                    delete(c)
            }
            if (!f.delete())
                throw FileNotFoundException("Failed to delete file: " + f)
        }
        delete(this.absoluteFile)
    })
}

fun File.listFileWithSuffix(suffix: String): List<String> {
    val filteredFiles = this.listFiles { file ->
        file.name.endsWith(suffix)
    }.map { it.absolutePath }.toList()
    val childrenFilteredFiles = this.listFiles { file ->
        file.isDirectory
    }.map { it.listFileWithSuffix(suffix) }.flatten()
    return childrenFilteredFiles + filteredFiles
}