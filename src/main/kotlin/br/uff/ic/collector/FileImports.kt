package br.uff.ic.collector

data class FileImports(
    val file: String,
    val imports: Collection<String>
)