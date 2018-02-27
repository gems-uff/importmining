package br.uff.ic.domain

import br.uff.ic.collector.JavaFile
import br.uff.ic.extensions.listFilesRecursively
import br.uff.ic.extensions.orNull
import io.netty.util.internal.ConcurrentSet
import java.io.File
import kotlin.streams.toList

@Suppress("JoinDeclarationAndAssignment")
class Project(val location: File) {

    /**
     * source files' paths
     *
     * assumptions:
     * contains the absolute file paths of all source files in the project for a given language
     * does not contain duplicates
     * does not contain test files
     * */
    val sourcePaths : Set<String>
        get() = location
                .listFilesRecursively { file ->
                    file.name.endsWith(".java") &&
                    !file.absolutePath.contains("test")
                }.toSet()

    /**
     * java files representation listed
     * assumptions:
     * contains the files which paths are in @param sourcePaths, instantiated to File(s)
     * does not contain duplicates
     * all files are compilable
     * */
    val sourceFiles: List<SourceFile?> = sourcePaths
            .parallelStream()
            .map {
                orNull {
                    SourceFile(File(it), this)
                }
            }.filter { it != null }
            .toList()


    /**
     * project packages' paths listed
     * assumptions:
     * contains packages from the given project only
     * does not contain duplicates
     * does not contain empty packages
     * */
    val packages : List<String> = sourceFiles
            .parallelStream()
            .map{ it!!.packageName }
            .filter{ it.isNotEmpty() }
            .toList()

    /**
     * imports from this project to this project
     * assumptions:
     * does not contain duplicates
     * all entries are in @param sourcePaths
     * */
    val imports : ConcurrentSet<String>
        get() {
            ConcurrentSet<String>().apply {
                sourceFiles
                .parallelStream()
                .map {
                    val local = it!!.imports.filter { clazz ->
                        packages.any {
                            clazz.contains(it)
                        }
                    }.toSet()
                    addAll(local)
                }
            }.let { return it }
        }

    /**
     * Returns true if the class given by @param clazz is a class of declared in this project
     * assumptions:
     * className is a simple class name
     * packages are absolute class names with package paths @see packages
     * */
    fun isFromThisProject(className : String) : Boolean =
            packages.parallelStream().anyMatch{ className.contains(it) }

    /*fun removeExternalImports() : List<SourceFile>{
        sourceFiles.parallelStream().map {
            val local = it!!.imports.filter { clazz ->
                packages.any {
                    clazz.contains(it)
                }
            }.toSet()
            it.copy(imports = local)
        }.filter {
            it.imports.isNotEmpty()
        }.toList()
        .let { return it }
    }*/
}