package br.uff.ic.domain

import br.uff.ic.extensions.listFilesRecursively
import br.uff.ic.extensions.orNull
import java.io.File
import kotlin.streams.toList

data class Project(val location: File,
              val sourcePaths : List<String>,
              val sourceFiles: List<SourceFile>,
              val packages : List<String>,
              val imports : List<String>) {

    /**
     * Returns true if the class given by @param clazz is a class of declared in this project
     * assumptions:
     * className is a simple class name
     * packages are absolute class names with package paths @see packages
     * */
    fun defines(className : String) : Boolean {
        if(packages.count() == 0) throw IllegalStateException("Packages have not been parsed yet.")
        return packages.any{ className.contains(it) }
    }

    fun listMainSourcePaths() : Project {
        if(!location.exists()) throw IllegalStateException("Project location is invalid.")
        val paths = location.listFilesRecursively { file -> file.name.endsWith(".java") && !file.absolutePath.contains("test") }
        return this.copy(sourcePaths = paths)
    }

    fun parseSourceFiles() : Project {
        if(sourcePaths.count() == 0) throw IllegalStateException("Source paths have not been resolved.")
        val sourceFiles = sourcePaths.parallelStream()
                .map { orNull { SourceFile(File(it), this, "", listOf()) } }
                .filter { it != null}
                .map { it!!.parseSource() }
                .distinct()
                .toList()
        return this.copy(sourceFiles = sourceFiles)
    }

    fun listPackages() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")
        val packages = sourceFiles
                .map{ it.packageName }
                .filter{ it.isNotEmpty() }
                .distinct()
                .toList()

        return this.copy(packages = packages)
    }

    fun removeExternalImports() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")

        val srcFiles = sourceFiles
                .map { it.removeExternalImports() }
                .filter { it.imports.isNotEmpty() }
                .toList()

        return this.copy(sourceFiles = srcFiles)
    }

    // TODO: trocar uso de packages.any para isFromThisProject
    fun findLocalImports() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")

        val imports = this.sourceFiles
                .flatMap { it.imports }
                .distinct()
                .toList()
        return this.copy(imports = imports)
    }

    /*srcs.parallelStream()
    .map {
        val local = it!!.imports.filter { clazz ->
            projectPackages.any {
                clazz.contains(it)
            }
        }.toSet()
        localImports.addAll(local)
        it.copy(imports = local)
    }.filter { it.imports.isNotEmpty() }.toList()*/
}