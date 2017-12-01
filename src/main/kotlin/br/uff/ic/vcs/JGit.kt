package br.uff.ic.vcs

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import org.eclipse.jgit.api.Git
import java.io.File

class JGit(private val directory: File) : VCS {

    //private companion object : Logger by LoggerFactory.new(JGit::class.java.canonicalName)

    override fun clone(uri: String): File {
        //info("Cloning the repository")
        try {
            Git.cloneRepository()
                .setURI(uri)
                .setDirectory(directory)
                .call()
        } catch (e: Exception) {
            //error("Could not clone the repository: $uri. Status=${e.message}")
            throw Exception("Could not clone the repository: $uri. Status=${e.message}")
        }
        return directory
    }
}