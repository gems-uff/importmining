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
        return this.copy(sourcePaths = paths)//Project(location, paths, sourceFiles, packages, imports)
    }

    fun parseSourceFiles() : Project {
        if(sourcePaths.count() == 0) throw IllegalStateException("Source paths have not been resolved.")
        val sourceFiles = sourcePaths.parallelStream()
                .map { orNull { SourceFile(File(it), "", listOf()).parseSource() } }
                .filter { it != null}
                .map { it!! }
                .distinct()
                .toList()
        return this.copy(sourceFiles = sourceFiles)//Project(location, sourcePaths, sourceFiles, packages, imports)
    }

    fun listPackages() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")
        val packages = sourceFiles.parallelStream()
                .map{ it.packageName }
                .filter{ it.isNotEmpty() }
                .distinct()
                .toList()

        return this.copy(packages = packages)//Project(location, sourcePaths, sourceFiles, packages, imports)
    }

    fun removeExternalImports() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")

        val srcFiles = sourceFiles.parallelStream()
                .map { it.removeExternalImports(this) }
                .filter { it.imports.isNotEmpty() }
                .toList()

        return this.copy(sourceFiles = srcFiles)//Project(location, sourcePaths, srcFiles, packages, imports)
    }

    fun findLocalImports() : Project {
        if(sourceFiles.count() == 0) throw IllegalStateException("Source Files have not been parsed yet.")

        val imports = this.sourceFiles
                .flatMap { it.imports }
                .parallelStream()
                .distinct()
                .toList()
        return this.copy(imports = imports)//Project(location, sourcePaths, sourceFiles, packages, imports) //
    }
}