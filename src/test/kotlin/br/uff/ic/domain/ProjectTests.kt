package br.uff.ic.domain

import org.amshove.kluent.*
import org.junit.Test
import java.io.File

class ProjectTests {

    private val projectLocation : File = File("D:\\Geral\\Desenvolvimento\\Kotlin")
    private val projectRepository : String = "https://github.com/apache/tomcat" // TODO: put a smaller repository
   // private val project : Project = cloneRepository(projectRepository, projectLocation)

    /* Source Paths */
/*
    @Test fun testProjectsSourcePathsNotEmpty() =
            project.sourcePaths
                    .shouldNotBeEmpty()
*/
    /*@Test fun testProjectsSourcePathsEncompassWholeProjectsSources() =
            foldOnFolder(mutableSetOf(),
                    ".java",
                    projectLocation,
                    {file -> file.name}
            ).shouldContainAll(project.sourcePaths)

    @Test fun testProjectsSourcePathsDoesNotContainsTestSources() =
            project.sourcePaths
                    .all { !it.contains("test") && !it.contains("Test") && !it.contains("TEST")}
                    .shouldBeTrue()

    /* Source Files */

    @Test fun testProjectsSourceFilesNotEmpty() =
            project.sourceFiles
                    .shouldNotBeEmpty()

    @Test fun testProjectsSourceFilesEncompassWholeProjectsSources() =
            project.sourceFiles
                    .all { project.sourcePaths.contains(it.getFilePath()) }
                    .shouldBeTrue()

    @Test fun testProjectsSourceFilesDoesNotContainDuplicates() =
            project.sourceFiles
                    .distinct()
                    .count()
                    .shouldEqualTo(project.sourceFiles.count())

    @Test fun testProjectsSourceFilesAreAllCompilable() =
            project.sourceFiles
                    .count()
                    .shouldEqualTo(project.sourcePaths.count())

    /* Packages */

    @Test fun testProjectsPackagesNotEmpty() =
            project.packages
                    .shouldNotBeEmpty()

    @Test fun testProjectsPackagesEncompassWholeProjectsPackages() =
            // TODO: test this
            foldOnFolder(mutableSetOf(),
                    ".java",
                    projectLocation,
                    {file -> file.absolutePath
                            .substringAfterLast(projectLocation.absolutePath)
                            .replace('\\', '.')
                            .replace(".java", "")
                            .substringBeforeLast('.')
                    }
            ).shouldContainAll(project.packages)

    @Test fun testProjectsPackagesHasNoEmptyPackage() =
            project.packages
                    .map { File(it.replace('.', '\\')) }
                    .map { it.listFiles { file -> file.name.endsWith(".java") } }
                    .all { it.count() > 0 }
                    .shouldBeTrue()

    @Test fun testProjectsPackagesDoesNotContainDuplicates() =
            project.packages
                    .distinct()
                    .count()
                    .shouldEqualTo(project.packages.count())

    /* Imports */

    @Test fun testProjectsImportsNotEmpty() =
            project.imports
                    .shouldNotBeEmpty()

    @Test fun testProjectsImportsAreReallyInternal() =
            project.imports
                    .map { "${projectLocation.absolutePath}\\${it.replace('.', '\\')}" }
                    .all { project.sourcePaths.contains(it) }
                    .shouldBeTrue()

    /* is from this project */

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