package br.uff.ic.mining

import br.uff.ic.domain.Project
import br.uff.ic.domain.Tests
import br.uff.ic.extensions.createIfNotExists
import br.uff.ic.vcs.SystemGit
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.BeforeClass
import org.junit.Test
import java.io.File

internal class DataSetTest {

    private val projectLocation = File("C:\\importmining\\test\\ongoing")

    companion object {
        private const val projectRepository = "https://github.com/apache/cassandra"
        private val projectLocation = File("C:\\importmining\\test\\ongoing")

        @JvmStatic @BeforeClass
        fun prepare(){
            SystemGit.clone(projectLocation.createIfNotExists(), projectRepository)
        }
    }

    @Test
    fun learnFrequentItemsetsNotEmpty() =
            Project(projectLocation, Tests.INCLUDE)
                    .let {
                        DataSet(it.imports, it.sourceFiles)
                    }.learnFrequentItemsets(.01).shouldNotBeEmpty()

}