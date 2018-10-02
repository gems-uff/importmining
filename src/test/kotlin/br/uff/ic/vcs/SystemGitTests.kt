package br.uff.ic.vcs

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class SystemGitTests {
    private val repoUri = "https://github.com/mpjmuniz/ufflabweb"
    private val fakeRepoUri = "https://github.coma/mpjmuniz/ufflabweb"
    private val privateRepoUri = "https://github.com/gems-uff/importmining"
    private val dir = File("C:\\importmining\\test\\ongoing")
    private val fakeDir = File("C:\\importmining\\test\\ongoinga")

    @Before fun prepare() {
        dir.mkdir()
    }

    @After fun tearDown() {
        dir.deleteRecursively()
    }

    @Test fun cloneExistingPublicRepoValidDir() =
        SystemGit.clone(dir, repoUri)
                .listFiles()
                .shouldNotBeEmpty()

    @Test(expected = Exception::class) fun cloneNonExistingRepoValidDir() =
        SystemGit.clone(dir, fakeRepoUri)
                .listFiles()
                .shouldBeEmpty()

    @Test(expected = Exception::class)
    fun cloneExistingPublicRepoInvalidDir() {
        SystemGit.clone(fakeDir, repoUri)
                .listFiles()
    }

    /* Hangs execution waiting for credentials
    @Test fun clonePrivateRepoValidDir() =
        SystemGit.clone(dir, privateRepoUri)
                .listFiles()
                .shouldNotBeEmpty()
                */

    @Test fun cloneClonedRepoValidDir() =
        SystemGit.clone(SystemGit.clone(dir, repoUri), repoUri)
                .listFiles()
                .shouldNotBeEmpty()
}