package br.uff.ic

import br.uff.ic.collector.CSVChannel
import br.uff.ic.collector.ExplicitImportCollector
import br.uff.ic.collector.Project
import br.uff.ic.io.deleteOnShutdown
import br.uff.ic.vcs.SystemGit
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.cli.*
import java.io.File
import java.nio.file.Files


object ImportMining {
    fun execute(args: Array<String>) {
        val options = Options()
        with(options) {
            addOptionGroup(OptionGroup().let { group ->
                addOption(
                        "r",
                        "repository",
                        true,
                        """The repository name on github. i. e. 'gems-uff/importmining'. Use this if you want to clone a repo. Specify this OR the 'repository-directory' option."""
                )
                addOption(
                        "d",
                        "repository-directory",
                        true,
                        """The UNIX-like repository absolute path. i. e. '/path/to/repo'. Use this if you want to use a repository that is already been cloned.  Specify this OR the 'repository' option"""
                )
                group
            })
            addRequiredOption(
                    "o",
                    "output",
                    true,
                    "The UNIX-like output absolute path."
            )
        }
        try {
            val parser = DefaultParser()
            val cmd: CommandLine = parser.parse(options, args)
            val repoDir: File
            if (cmd.hasOption("r")) {
                repoDir = with(Files.createDirectory(File("temp${System.nanoTime()}").toPath()).toFile()) {
                    deleteOnShutdown()
                    setReadable(true, true)
                    setWritable(true, true)
                    if (!exists()) {
                        error("Could not create directory")
                    }
                    this
                }
                val repoUrl = "https://github.com/${cmd.getOptionValue("r")}.git"
                println("Cloning repo: $repoUrl")
                SystemGit().clone(repoUrl, repoDir)
            } else {
                repoDir = File(cmd.getOptionValue("d"))
            }
            val output = File(cmd.getOptionValue("output"))
            runBlocking {
                ExplicitImportCollector(CSVChannel()).collect(Project(repoDir), output)
            }
        } catch (e: ParseException) {
            println(e.message)
            val formatter = HelpFormatter()
            formatter.printHelp("import-mining", options)
            System.exit(1)
        } catch (e: Exception) {
            println(e.message)
        }

    }


}

fun main(args: Array<String>) {
    ImportMining.execute(
            "-r kohsuke/args4j -o out.csv".split(" ").toTypedArray()
    )
}