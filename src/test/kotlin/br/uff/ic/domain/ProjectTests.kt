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
        Project(projectLocation, IncludeTests.INCLUDE)
                .sourcePaths
                .shouldNotBeEmpty()

    @Test fun projectSourceFilesNotEmpty() =
        Project(projectLocation, IncludeTests.INCLUDE)
                .sourceFiles
                .shouldNotBeEmpty()

    @Test fun projectPackagesNotEmpty() =
        Project(projectLocation, IncludeTests.INCLUDE)
                .packages
                .shouldNotBeEmpty()

    @Test fun projectImportsNotEmpty() =
        Project(projectLocation, IncludeTests.INCLUDE)
                .imports
                .shouldNotBeEmpty()

    @Test fun projectSourceFilesHaveWholeProjectSources() =
        Project(projectLocation, IncludeTests.INCLUDE)
                .let {
                    it.sourceFiles.count().shouldEqualTo(it.sourcePaths.count())
                }

    @Test fun projectSourceFilesDoesNotContainDuplicates() =
        Project(projectLocation, IncludeTests.INCLUDE)
                .let {
                    it.sourceFiles.distinct().count().shouldEqualTo(it.sourceFiles.count())
                }

    @Test fun projectPackagesDoesntHaveDuplicates() =
        Project(projectLocation, IncludeTests.INCLUDE)
            .let {
                it.packages.distinct().count().shouldEqualTo(it.packages.count())
            }

    /*@Test fun testProjectsSourcePathsDoesNotContainsTestSources() =
            Project(projectLocation, IncludeTests.INCLUDE)
                .let {
                    it.sourcePaths
                        .all { !it.contains("test") && !it.contains("Test") && !it.contains("TEST")}
                        .shouldBeTrue()
                }*/
    @Test fun testProjectsSourceFilesEncompassWholeProjectsSources() =
            Project(projectLocation, IncludeTests.INCLUDE)
                    .let { proj ->
                        proj.sourceFiles
                        .all { proj.sourcePaths.contains(it.getFilePath()) }
                        .shouldBeTrue()
                    }

    /*
    @Test fun projectImportsAreReallyInternal() = TODO()

    @Test fun isFromThisProjectOnFakeClass() = TODO()

    @Test fun isFromThisProjectOnRealClass() = TODO()
    */
/*
    @Test fun testProjectsPackagesHasNoEmptyPackage() =
            project.packages
                    .map { File(it.replace('.', '\\')) }
                    .map { it.listFiles { file -> file.name.endsWith(".java") } }
                    .all { it.count() > 0 }
                    .shouldBeTrue()

    @Test fun testProjectsImportsAreReallyInternal() =
            project.imports
                    .map { "${projectLocation.absolutePath}\\${it.replace('.', '\\')}" }
                    .all { project.sourcePaths.contains(it) }
                    .shouldBeTrue()


    @Test fun testIsFromThisProjectFakeClass() =
            project.isFromThisProject(this.javaClass.canonicalName)
                    .shouldBeFalse()

    @Test fun testIsFromThisProjectRealClass() =
            foldOnFolder(mutableSetOf(),
                    ".java",
                    projectLocation,
                    {file -> file.name.replaceBeforeLast('.', "\\")}
            ).first()
                    .let {
                        project.isFromThisProject(it)
                    }*/
}