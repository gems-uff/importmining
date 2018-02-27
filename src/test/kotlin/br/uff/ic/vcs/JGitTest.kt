package br.uff.ic.vcs

import br.uff.ic.domain.Project
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class JGitTest {

    // TODO: test clone working on public repos
    // TODO: test clone working on private repos
    // TODO: test repository folder location accessible
    private val repoLocation : String = "https://github.com/mpjmuniz/Sheeprom-Beta"
    private val projectLocation : String = "D:\\Geral\\Desenvolvimento\\Kotlin"
    private lateinit var project : Project

    @Before fun cloneRepository(){
        project = cloneRepository(repoLocation, File(projectLocation))
    }

    @Test fun testNotEmptyRepository(){
        Assert.assertTrue(project.sourceFiles.isNotEmpty())
    }
}