package br.uff.ic.vcs

import br.uff.ic.domain.Project
import java.io.File
/*
class SystemGit(private val directory: File){

    fun clone(uri: String): Project {
        if(directory.listFiles().count() <= 1){
            val rt = Runtime.getRuntime()
            val cmd = "git clone --depth=1 $uri ${directory.absolutePath}"
            val pr = rt.exec(cmd)
            val status = pr.waitFor()
            if (status != 0) {
                throw Exception("Could not clone the repository: $uri. Status=$status")
            }
        }
        return Project(directory)
    }
}*/