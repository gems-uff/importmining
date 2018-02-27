import br.uff.ic.collector.ExplicitImportCollector
import br.uff.ic.domain.Project
import br.uff.ic.logger.ConsoleHandler
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.FPGrowthRuleExtractor
import br.uff.ic.mining.Rule
import br.uff.ic.pipelines.JsonBucket
import br.uff.ic.vcs.SystemGit
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.experimental.runBlocking
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import java.io.File
import kotlin.system.measureTimeMillis


object ImportMining {

    // TODO: read about
    val sparkContext: JavaSparkContext
    private val tempDirectory : File
    private val bucketLocation : String
    private val repositoryUri : String
    private val logger : Logger

    init {
        LoggerFactory.addHandler(ConsoleHandler())
        val sparkConf = SparkConf()
                .setAppName("ImportMining")
                .setMaster("local[8]")
                .set("spark.executor.memory", "1g")
        sparkContext = JavaSparkContext(sparkConf)
        tempDirectory = File("E:\\Pessoal\\Dev\\importmining\\src\\main\\resources\\temp")
        bucketLocation = "E:\\Pessoal\\Dev\\importmining\\src\\main\\resources"
        repositoryUri = "https://github.com/apache/tomcat"
        logger = LoggerFactory.new(ImportMining::class.java)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        measureTimeMillis {
            val bucket = JsonBucket(bucketLocation)
            val vcs = SystemGit(tempDirectory)
            val collector = ExplicitImportCollector()
            val extractor = FPGrowthRuleExtractor(0.05,0.1)
            val project : Project
            val dataSet : DataSet
            val rules : Collection<Rule>
            //val couplings : Collection<Coupling>

            logger.info("cloning the repository: $repositoryUri @ $tempDirectory")
            project = vcs.clone(repositoryUri)
            logger.info("collecting imports information")
            dataSet = runBlocking { collector.collect(project) }
            logger.info("learning association rules from imports information")
            rules = extractor.extract(dataSet)
            logger.info("measuring coupling from rules")
            bucket.save("extracted-rules", rules)
            println("extracted")
        }.apply {
            logger.info("${toDouble() / 1000}")
        }

        /* measureTimeMillis {
             val bucket = JsonBucket(bucketLocation)
             val project : Project
             val dataSet : DataSet
             val rules : Collection<Rule>
             val couplings : Collection<Coupling>
             project = cloneRepository(repositoryUri, tempDirectory)
             dataSet = collectImports(project)
             rules = FPGrowthRuleExtractor(0.05, 0.1).extract(dataSet)
             couplings = measureCouplingsInformation(rules)
             bucket.save("couplings-found", couplings)
             // TODO: analyze possbile metrics based on coupling values generated by rules
             // TODO: analyze threshold values to check for reasonable number of rules (at least 10 instances should be good)
             //tempDirectory.deleteRecursively() // TODO: make option to keep files
         }.let { time ->
             logger.info("${time.toDouble() / 1000}")
         }*/
    }
}