package br.uff.ic

import br.uff.ic.collector.ImportCollector
import br.uff.ic.collector.Project
import br.uff.ic.io.deleteOnShutdown
import br.uff.ic.logger.ConsoleHandler
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.featureselection.FeatureSelector
import br.uff.ic.mining.ruleextraction.RuleExtractor
import br.uff.ic.vcs.JGit
import br.uff.ic.vcs.SystemGit
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.cli.*
import weka.core.converters.ArffLoader
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files

data class ExperimentSpecification(
    val featureSelectors: List<Any>?,
    val ruleExtractors: Map<Any, Any>?,
    val importCollector: Map<String, String>
)

object ImportMining {
    @JvmStatic
    @JvmName("main")
    fun execute(args: Array<String>) {
        LoggerFactory.addHandler(ConsoleHandler())
        val options = Options()
        with(options) {
            addRequiredOption(
                "es",
                "experiment-specification",
                true,
                "The path to the specification to the experiment. It must be UNIX-like."
            )
        }
        try {
            val parser = DefaultParser()
            val cmd: CommandLine = parser.parse(options, args)
            with(BufferedReader(FileReader(cmd.getOptionValue("es")))) {
                val json = readText()
                val mapper = ObjectMapper().registerKotlinModule()
                val spec = mapper.readValue<ExperimentSpecification>(json)
                var repoDir = with(Files.createDirectory(File("temp${System.nanoTime()}").toPath()).toFile()) {
                    deleteOnShutdown()
                    setReadable(true, true)
                    setWritable(true, true)
                    if (!exists()) {
                        error("Could not create new directory")
                    }
                    this
                }
                val preprocessed = spec.importCollector["preprocessed"]
                val arff: String
                if (preprocessed != null) {
                    arff = preprocessed
                } else {
                    val url = spec.importCollector["url"]
                    val name = spec.importCollector["name"]
                    val path = spec.importCollector["path"]
                    when {
                        url != null -> SystemGit().clone(url, repoDir)
                        name != null -> SystemGit().clone("https://github.com/$name.git", repoDir)
                        path != null -> repoDir = File(path)
                    }

                    val collector = ImportCollector.new(spec.importCollector)
                    runBlocking {
                        collector.collect(Project(repoDir))
                    }
                    arff = spec.importCollector["output"]!!
                }
                if (spec.featureSelectors != null && spec.ruleExtractors != null){
                    val selector = FeatureSelector.new(spec.featureSelectors)
                    val extractor = RuleExtractor.new(spec.ruleExtractors)
                    val instances = ArffLoader.ArffReader(BufferedReader(FileReader(arff))).data
                    instances.setClassIndex(-1)
                    extractor.extract(selector.select(instances))
                }

            }
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

    private fun analyze(expResult : List<String>) {
        // Receaves a list as the process runs in a pipeline spitting more than one output as it goes
        // In the end all of them should be grouped and passed here
        runBlocking {
            expResult.forEach {
                with(BufferedReader(FileReader(it))) {
                    val json = readText()
                    val mapper = ObjectMapper().registerKotlinModule()
                    val result : Array<Any>? = mapper.readValue(json)

                    if (result != null && !result.isEmpty()) {
                        // TODO: implement general result collection
                    }
                }
            }
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
            //            ExplicitImportCollector(CSVChannel()).collect(Project(repoDir), output)
        }
    }


}

fun main(args: Array<String>) {
    ImportMining.execute(
            "-es /home/mralves/Projects/kotlin/importmining/src/main/resources/config.json".split(" ").toTypedArray()
    )
}