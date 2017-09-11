package br.uff.ic.vcs

import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import com.jcraft.jsch.Session
import com.jcraft.jsch.UserInfo
import java.io.File
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.TransportConfigCallback
import org.eclipse.jgit.transport.*
import org.eclipse.jgit.util.FS
import org.eclipse.jgit.transport.SshTransport
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.io.IOException
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.SimpleFileVisitor
import org.bounce.util.URIUtils.toFile
import java.nio.file.Path


class JGit : VCS  {
    private companion object : Logger by LoggerFactory.new(JGit::class.java.canonicalName)



    override fun clone(uri: String, directory: File) {
        info("Cloning the repository")

        var clone : Git? = null

        try {
            clone = Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(directory)
                    .call()

        } catch (e : Exception) {
            warn("Either repository is private or inaccessible. Message: ${e.message}")

            /*
            Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(directory)
                    .setTransportConfigCallback {  TODO: implement authentication  }
                    .call()
            */

            throw Exception("Could not clone the repository: $uri. Status=${e.message}")
        } finally {

            clone?.close()
        }
    }


}