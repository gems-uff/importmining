package br.uff.ic.domain

import br.uff.ic.extensions.listFilesRecursively
import br.uff.ic.extensions.orNull
import java.io.File
import kotlin.streams.toList

data class Project(private val location:     File,
                   private val includeTests: IncludeTests) {

   val sourcePaths : List<String>
   val sourceFiles : List<SourceFile>
   val packages    : List<String>
   val imports     : List<String>

   init {
       sourcePaths = location.listFilesRecursively { file -> includeTests.getPredicate(file)}
       val allSourceFiles = sourcePaths.parallelStream()
               .map { orNull { SourceFile(File(it), "", listOf()).parseSource() } } // TODO: verify
               .filter { it != null}
               .map { it!! }
               .distinct()
               .toList()

       packages = allSourceFiles.parallelStream()
               .map{ it.packageName }
               .filter{ it.isNotEmpty() }
               .distinct()
               .toList()

       sourceFiles = allSourceFiles.parallelStream()
               .map { it.removeExternalImports(this) } // TODO: verify
               //.filter { it.imports.isNotEmpty() }
               .toList()

       imports = this.sourceFiles
               .flatMap { it.imports }
               .parallelStream()
               .distinct()
               .toList()
   }
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
}