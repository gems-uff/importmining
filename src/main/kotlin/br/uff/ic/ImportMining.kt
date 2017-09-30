import br.uff.ic.collector.ExplicitImportCollector
import br.uff.ic.mining.DataSet
import br.uff.ic.mining.FPGrowthRuleExtractor
import br.uff.ic.pipelines.ExtractRulesPipeline
import br.uff.ic.pipelines.JsonBucket
import br.uff.ic.vcs.SystemGit
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext


object ImportMining {

    val sparkContext: JavaSparkContext

    init {
        val sparkConf = SparkConf()
                .setAppName("SOME APP NAME")
                .setMaster("local[2]")
                .set("spark.executor.memory", "1g")
        sparkContext = JavaSparkContext(sparkConf)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        val vcs = SystemGit(File("/home/mralves/Projects/kotlin/import-mining/trash"))
        val collector = ExplicitImportCollector()
        val extractor = FPGrowthRuleExtractor(
                0.1,
                0.7
        )
        val bucket = JsonBucket(
                "/home/mralves/Projects/kotlin/import-mining/src/main/resources",
                ObjectMapper().registerKotlinModule()
        )
        val pipeline = ExtractRulesPipeline(vcs, collector, extractor, bucket)
        pipeline.execute("https://github.com/apache/commons-cli")
    }
}

fun DataSet.toRDD(): JavaRDD<List<String>> {
    val imports = this.data.map { (_, imp) ->
        imp.toList()
    }
    return ImportMining.sparkContext.parallelize(imports)
}