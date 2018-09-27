package br.uff.ic.domain

import br.uff.ic.extensions.createIfNotExists
import br.uff.ic.vcs.SystemGit
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

    private val sourcePath : File = File("C:\\importmining\\src\\test\\resources\\TestSourceFile.java")

    /*
    * called from whole constructor does not execute javaparser
    * called from second constructor works with javaparser
    * imports are all from this project
    * imports are not empty (for a nonempty importlist class)
    * package is the . separated package name
    * */

    @Test fun sourceFileImportsAreProperlyInitialized() =
            SourceFile(sourcePath).imports.shouldNotBeEmpty()

    @Test fun sourceFilePackageIsPropertyInitialized() =
            SourceFile(sourcePath).packageName.shouldNotBeEmpty()
}