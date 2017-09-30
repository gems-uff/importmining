package br.uff.ic.vcs

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import java.io.File
import org.eclipse.jgit.api.Git
//
//class JGit : VCS  {
//    private companion object : Logger by LoggerFactory.new(SystemGit::class.java.canonicalName)
//
//    override fun clone(uri: String, directory: File) {
//        info("Cloning the repository")
//
//        try {
//            Git.cloneRepository()
//                    .setURI(uri)
//                    .setDirectory(directory)
//                    .call()
//        } catch (e : Exception) {
//            error("Could not clone the repository: $uri. Status=${e.message}")
//            throw Exception("Could not clone the repository: $uri. Status=${e.message}")
//        }
//    }
//}