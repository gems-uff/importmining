import br.uff.ic.collector.ExplicitImportCollector
import br.uff.ic.logger.ConsoleHandler
import br.uff.ic.logger.LoggerFactory
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.FPGrowthRuleExtractor
import br.uff.ic.pipelines.AnalyzeProjectPipeline
import br.uff.ic.pipelines.JsonBucket
import br.uff.ic.vcs.SystemGit
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import java.io.File
import kotlin.system.measureTimeMillis


object ImportMining {

    val sparkContext: JavaSparkContext

    init {
        val sparkConf = SparkConf()
            .setAppName("ImportMining")
            .setMaster("local[8]")
                .set("spark.executor.memory", "1g")
        sparkContext = JavaSparkContext(sparkConf)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        LoggerFactory.addHandler(ConsoleHandler())
        val vcs = SystemGit(File("/home/mralves/Projects/kotlin/importmining/trash"))
        val collector = ExplicitImportCollector()
        val extractor = FPGrowthRuleExtractor(
            0.05,
            0.1
        )
        val bucket = JsonBucket(
            "/home/mralves/Projects/kotlin/importmining/src/main/resources",
                ObjectMapper().registerKotlinModule()
        )
        val pipeline = AnalyzeProjectPipeline(vcs, collector, extractor, bucket)

        val time = measureTimeMillis {
            pipeline.execute("https://github.com/hibernate/hibernate-orm")
        }

        println("${time.toDouble() / 1000}")

    }
}

fun DataSet.toRDD(): JavaRDD<List<String>> {
    val imports = this.data.map { (_, imp) ->
        imp.toList()
    }
    return ImportMining.sparkContext.parallelize(imports)
}