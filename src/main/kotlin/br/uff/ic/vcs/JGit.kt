package br.uff.ic.vcs

import br.uff.ic.domain.Project
import org.eclipse.jgit.api.Git
import java.io.File

/**
 * Clones a git repository
 *
 * @param directory the local directory file reference, location to be saved the cloned repository
 * @param uri the location on the web of the git repository
 *
 * @return the master branch's latest version of the specified project
 * */
fun cloneRepository(uri: String, directory: File): Project {
    if(directory.listFiles().count() <= 1) {
        try {
            Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(directory)
                    .call()
        } catch (e: Exception) {
            throw Exception("Could not clone the repository: $uri. Status=${e.message}")
        }
    }
    return Project(directory)
}