import br.uff.ic.domain.CouplingInfo
import br.uff.ic.domain.Project
import br.uff.ic.domain.Tests
import br.uff.ic.extensions.createIfNotExists
import br.uff.ic.logger.ConsoleHandler
import br.uff.ic.logger.Logger
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.Itemset
import br.uff.ic.pipelines.CSVCouplingWriter
import br.uff.ic.pipelines.JsonBucket
import br.uff.ic.vcs.SystemGit
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import java.io.File
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

object ImportMining {

    val sparkContext: JavaSparkContext
    private val tempDirectory: String
    private val logger: Logger

    // Loads logger, spark context and temp location used
    init {
        LoggerFactory.addHandler(ConsoleHandler())
        val sparkConf = SparkConf()
                .setAppName(APP_NAME)
                .setMaster("local[8]")
                .set("spark.executor.memory", "1g")
        sparkContext = JavaSparkContext(sparkConf)
        tempDirectory = "$LOCATION\\temp"
        logger = LoggerFactory.new(ImportMining::class.java)
    }

    @Suppress("JoinDeclarationAndAssignment")
    @JvmStatic
    fun main(arguments: Array<String>) {
        val args = ArgParser(arguments)
        val bucketLocation by args.storing("-l", "--location",
                help = "set output location")
                .default(LOCATION)
        val repositoryUri by args.storing("-u", "--uri",
                help = "set git repository URI")
                .default(URI)
        val deleteAtTheEnd by args.flagging("-d", "--delete",
                help = "should the project files analyzed be deleted after the execution?")
                .default(false)
        val includeTestFiles by args.flagging("-t", "--tests",
                help = "should tcouhe project's test files be taken into account?")
                .default(false)
        measureTimeMillis {
            val bucket = JsonBucket(bucketLocation)
            val location: File
            val project: Project
            val dataSet: DataSet
            val itemsets: Collection<Itemset>
            val couplings: Collection<CouplingInfo>
            val minimumSupport: Double
            val includeTests = if (includeTestFiles) Tests.INCLUDE else Tests.EXCLUDE

            logger.info("cloning the repository: $repositoryUri @ $tempDirectory")
            location = SystemGit.clone(File(tempDirectory).createIfNotExists(), repositoryUri)
            logger.info("constructing project")
            project = Project(location, includeTests)
            minimumSupport = BASE_SUPPORT
            logger.info("collecting imports information")
            dataSet = DataSet(project.imports, project.sourceFiles)
            logger.info("learning frequent itemsets from imports information")
            itemsets = dataSet.learnFrequentItemsets(minimumSupport)
            bucket.save("extracted-itemsets", itemsets)
            logger.info("measuring coupling from rules")
            couplings = dataSet.measureCouplingInformation(itemsets)
            //bucket.save("couplings-found", couplings)
            //TODO: remove common path name from class to the analysis, and make groupings for each common name in the class name trail
            //TODO: enumerate tests on each class
            CSVCouplingWriter("$bucketLocation\\couplings-found.csv").writeData(couplings)

            if (deleteAtTheEnd)
                File(tempDirectory)
                        .listFiles()
                        .forEach { it.deleteRecursively() }
        }.apply {
            logger.info("${toDouble() / 1000}")
        }
    }
}