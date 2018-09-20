package br.uff.ic.vcs

import br.uff.ic.domain.Project
import java.io.File
object SystemGit{
    fun clone(directory : File, uri: String): File {
        if(directory.listFiles().count() <= 1){
            Runtime.getRuntime()
                    .exec("git clone --depth=1 $uri ${directory.absolutePath}")
                    .waitFor()
                    .let { status ->
                        when(status) {
                            0    -> return directory
                            else -> throw Exception("Could not clone the repository: $uri. Status=$status")
                        }
                    }
        }
        return directory
    }
}