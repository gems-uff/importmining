package br.uff.ic.vcs

import org.junit.Assert
import org.junit.Test
import java.io.File
import java.nio.file.Files

class JGitTest {

    private val vcs : JGit = JGit(File("D:\\test"))

    // TODO: test clone working on public repos
    // TODO: test clone working on private repos
    // TODO: test repository folder location accessible

    @Test
    fun testExistingRepository(){
        val root = vcs.clone("https://github.com/JetBrains/kotlin")

        Assert.assertNotNull(root)
        Assert.assertTrue(root.exists())

        Files.deleteIfExists(root.toPath())
    }
}