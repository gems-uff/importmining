package br.uff.ic.vcs

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import java.io.File

class SystemGit(private val directory: File): VCS {
    private companion object : Logger by LoggerFactory.new(SystemGit::class.java.canonicalName)

    override fun clone(uri: String): File {
        directory.deleteRecursively()
        info("Cloning the repository")
        val rt = Runtime.getRuntime()
        val cmd = "git clone --depth=1 $uri ${directory.absolutePath}"
        debug("Running the following command: $cmd")
        val pr = rt.exec(cmd)
        val status = pr.waitFor()
        if (status != 0) {
            error("Could not clone the repository: $uri. Status=$status")
            throw Exception("Could not clone the repository: $uri. Status=$status")
        }
        return directory
    }

}