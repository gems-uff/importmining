package br.uff.ic.vcs

import java.io.File

class SystemGit : VCS {
    override fun clone(url: String, directory: File) {
        val rt = Runtime.getRuntime()
        val cmd = "git clone $url ${directory.absolutePath}"
        val pr = rt.exec(cmd)
        val status = pr.waitFor()
        if (status != 0) {
            error("Could not clone the repository: $url. Status=$status")
        }
    }

}