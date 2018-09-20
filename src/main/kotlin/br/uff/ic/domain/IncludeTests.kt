package br.uff.ic.domain

import java.io.File

enum class IncludeTests {
    INCLUDE {
        override fun getPredicate(file: File) : Boolean =
                file.name.endsWith(".java")
    },
    EXCLUDE {
        override fun getPredicate(file : File) : Boolean =
                file.name.endsWith(".java") && !file.absolutePath.contains("test")
    };

    abstract fun getPredicate(file : File): Boolean
}