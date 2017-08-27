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