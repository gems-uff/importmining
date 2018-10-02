package br.uff.ic.extensions

import br.uff.ic.vcs.SystemGit
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldExist
import org.junit.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class FileExtensionsTests {

    private val existingLocation = "C:\\importmining\\test"
    private val nonexistingLocation = "C:\\importmining\\test\\ongoing"

    // Create if not exists
    @Test fun callOnExistingDirectory() =
            File(existingLocation)
                    .createIfNotExists()
                    .shouldExist()

    @Test fun callOnInexistingDirectory() {
        File(nonexistingLocation).let {
            it.deleteOnExit()
            it.createIfNotExists()
              .shouldExist()
        }
    }

    //Listfiles recusively
    @Test fun listFilesRecursivelyBaseCaseTruePred() {
        File(existingLocation).let {
            it.deleteRecursively()
            it.createIfNotExists()
            for (i in 1..10) { File("$existingLocation\\file$i.txt").createNewFile() }
            val files = it.listFiles().map { it.absolutePath }
            it.listFilesRecursively { true }.shouldContainAll(files)
        }
    }

    private val repoURI = "https://github.com/mpjmuniz/ufflabweb"

    @Test fun listFilesRecursivelyInductionStepTruePred() {
        File(existingLocation).let {
                it.deleteRecursively()
                it.createIfNotExists()
                SystemGit.clone(it, repoURI)
                        .listFilesRecursively { true }
                        .shouldContainAll(listFilesRecursively(it))
        }
    }

    fun listFilesRecursively(file : File) : Iterable<String>{
        val filesThisDeep = file.listFiles().map { it.absolutePath }
        val filesDeeper = file.listFiles{ f -> f.isDirectory}.flatMap { f2 -> listFilesRecursively(f2) }
        return filesThisDeep + filesDeeper
    }

}