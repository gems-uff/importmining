package br.uff.ic.domain

import com.github.javaparser.JavaParser
import com.github.javaparser.ParseStart
import com.github.javaparser.Providers
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.io.FileInputStream

class SourceFileTests {

    private lateinit var sourceFile : SourceFile
    private val sourcePath : File = File("")
    private val project : Project = Project(File(""))

    @BeforeClass fun parseSourceFile() {
        sourceFile = SourceFile(sourcePath, project)
    }
    /*
    * called from whole constructor does not execute javaparser
    * called from second constructor works with javaparser
    * imports are all from this project
    * imports are not empty (for a nonempty importlist class)
    * package is the . separated package name
    * */

    @Test fun creationWithoutJavaParser() =
            SourceFile(sourcePath, listOf(), "")
                    .let {
                        it.packageName == "" &&
                                it.imports.isEmpty() &&
                                it.getFilePath() == sourcePath.absolutePath
                    }.shouldBeTrue()

    @Test fun creationWithJavaParser() =
            SourceFile(sourcePath, project)
                    .let {
                        it.packageName == SourceFile.PackageVisitor()
                                .visit(JavaParser()
                                        .parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(sourcePath)))
                                        .result.get(), null) &&
                                it.imports == SourceFile.ImportVisitor()
                                        .apply { visit(JavaParser()
                                                .parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(sourcePath)))
                                                .result.get(), null) }.imports
                        it.getFilePath() == sourcePath.absolutePath
                    }.shouldBeTrue()

    private class TestImportVisitor(val imports : MutableList<String>) : GenericListVisitorAdapter<String, Void>() {
        override fun visit(n: ImportDeclaration, arg: Void?): MutableList<String>? =
                imports.apply{ add(n.nameAsString) }
    }

    @Test fun importsAreProperlyInitialized() =
            (SourceFile(sourcePath, project).imports == TestImportVisitor(mutableListOf()).visit(JavaParser()
                    .parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(sourcePath)))
                    .result.get(), null))
                    .shouldBeTrue()

    @Test fun importsAreRealyLocal() =
            project.imports.shouldContainAll(SourceFile(sourcePath, project).imports)

    @Test fun importsAreNotEmpty() =
            SourceFile(sourcePath, project)
                    .imports
                    .shouldNotBeEmpty()

    @Test fun packageNameCorrespondsToDeclaration() =
            (SourceFile(sourcePath, project).packageName == SourceFile.PackageVisitor()
                    .visit(JavaParser()
                            .parse(ParseStart.COMPILATION_UNIT, Providers.provider(FileInputStream(sourcePath)))
                            .result.get(), null))
                    .shouldBeTrue()
}