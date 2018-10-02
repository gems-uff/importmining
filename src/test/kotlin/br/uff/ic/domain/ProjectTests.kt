package br.uff.ic.domain

import br.uff.ic.extensions.createIfNotExists
import br.uff.ic.vcs.SystemGit
import org.amshove.kluent.*
import org.junit.BeforeClass
import org.junit.Test
import java.io.File

class ProjectTests {

    private val projectLocation = File("C:\\importmining\\test\\ongoing")

    companion object {
        private const val projectRepository = "https://github.com/DuGuQiuBai/Java"
        private val projectLocation = File("C:\\importmining\\test\\ongoing")

        @JvmStatic @BeforeClass fun prepare(){
            SystemGit.clone(projectLocation.createIfNotExists(), projectRepository)
        }
    }

    @Test fun projectSourcePathsNotEmpty() =
        Project(projectLocation, Tests.INCLUDE)
                .sourcePaths
                .shouldNotBeEmpty()

    @Test fun projectSourceFilesNotEmpty() =
        Project(projectLocation, Tests.INCLUDE)
                .sourceFiles
                .shouldNotBeEmpty()

    @Test fun projectPackagesNotEmpty() =
        Project(projectLocation, Tests.INCLUDE)
                .packages
                .shouldNotBeEmpty()

    @Test fun projectImportsNotEmpty() =
        Project(projectLocation, Tests.INCLUDE)
                .imports
                .shouldNotBeEmpty()

    @Test fun projectSourceFilesHaveWholeProjectSources() =
        Project(projectLocation, Tests.INCLUDE)
                .let {
                    it.sourceFiles.count().shouldEqualTo(it.sourcePaths.count())
                }

    @Test fun projectSourceFilesDoesNotContainDuplicates() =
        Project(projectLocation, Tests.INCLUDE)
                .let {
                    it.sourceFiles.distinct().count().shouldEqualTo(it.sourceFiles.count())
                }

    @Test fun projectPackagesDoesntHaveDuplicates() =
        Project(projectLocation, Tests.INCLUDE)
            .let {
                it.packages.distinct().count().shouldEqualTo(it.packages.count())
            }

    /*@Test fun testProjectsSourcePathsDoesNotContainsTestSources() =
            Project(projectLocation, Tests.INCLUDE)
                .let {
                    it.sourcePaths
                        .all { !it.contains("test") && !it.contains("Test") && !it.contains("TEST")}
                        .shouldBeTrue()
                }*/
    @Test fun testProjectsSourceFilesEncompassWholeProjectsSources() =
            Project(projectLocation, Tests.INCLUDE)
                    .let { proj ->
                        proj.sourceFiles
                        .all { proj.sourcePaths.contains(it.getFilePath()) }
                        .shouldBeTrue()
                    }

    @Test fun testIsFromThisProjectFakeClass() =
            Project(projectLocation, Tests.INCLUDE)
                    .defines(this.javaClass.canonicalName)
                    .shouldBeFalse()

}