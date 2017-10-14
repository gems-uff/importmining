package br.uff.ic.extensions

import java.io.File

fun String.getJavaClassFile(projectRoot : String): File {
    return File("$projectRoot\\${this.replace('.', '\\')}.java" )
}

fun String.getKotlinClassFile(projectRoot: String): File{
    return File("$projectRoot\\${this.replace('.', '\\')}.kt" )
}