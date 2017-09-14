package br.uff.ic

import br.uff.ic.collector.CSVChannel
import br.uff.ic.collector.ExplicitImportCollector
import br.uff.ic.collector.Project
import br.uff.ic.io.deleteOnShutdown
import br.uff.ic.logger.ConsoleHandler
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.featureselection.PipelineFeatureSelector
import br.uff.ic.mining.ruleextraction.FPGrowthRuleExtractor
import br.uff.ic.vcs.JGit
import br.uff.ic.vcs.SystemGit
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.cli.*
import java.io.File
import java.nio.file.Files


object ImportMining {
    fun execute(args: Array<String>) {
        LoggerFactory.addHandler(ConsoleHandler())
        val options = Options()
        with(options) {
            addOption(
                "op",
                "operation",
                true,
                "Operation to be executed. Possible values are: collect, process, full"
            )
            addOptionGroup(OptionGroup().let { group ->
                group.addOption(
                    Option(
                        "r",
                        "repository",
                        true,
                        """The repository name on github. i. e. 'gems-uff/importmining'. Use this if you want to clone a repo. Specify this OR the 'repository-directory' option."""
                    )
                )
                group.addOption(
                    Option(
                        "d",
                        "repository-directory",
                        true,
                        """The UNIX-like repository absolute path. i. e. '/path/to/repo'. Use this if you want to use a repository that has already been cloned.  Specify this OR the 'repository' option"""
                    )
                )
                group.addOption(
                    Option(
                        "i",
                        "input",
                        true,
                        """The UNIX-like csv absolute path. i. e. '/path/to/repcsv'. Use this if you want to use a CSV file generated on a previous execution."""
                    )
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
            when (cmd.getOptionValue("op")) {
                "collect" -> {
                    collect(cmd)
                }
                "process" -> {
                    process(cmd)
                }
            }
        } catch (e: ParseException) {
            println(e.message)
            val formatter = HelpFormatter()
            formatter.printHelp("import-mining", options)
            System.exit(1)
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
        }

    }

    private fun process(cmd: CommandLine) {
        runBlocking {
            val output = File(cmd.getOptionValue("input"))

            //WekaRulerExtractor(WekaFeatureSelection()).extract(output.absolutePath)
        }
    }

    private fun collect(cmd: CommandLine) {
        val repoDir: File
        if (cmd.hasOption("r")) {
            repoDir = with(Files.createDirectory(File("temp${System.nanoTime()}").toPath()).toFile()) {
                deleteOnShutdown()
                setReadable(true, true)
                setWritable(true, true)
                if (!exists()) {
                    error("Could not create new directory")
                }
                this
            }
            val repoURI = "https://github.com/${cmd.getOptionValue("r")}.git"
            println("Cloning repo: $repoURI")
            JGit().clone(repoURI, repoDir)
        } else {
            repoDir = File(cmd.getOptionValue("d"))
        }
        val output = File(cmd.getOptionValue("output"))
        runBlocking {
            ExplicitImportCollector(CSVChannel()).collect(Project(repoDir), output)
        }
    }


}

fun main(args: Array<String>) {
    ImportMining.execute(
        "-op collect -r mpjmuniz/pspa-gcp -o out.json".split(" ").toTypedArray()
    )
}