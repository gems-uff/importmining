package br.uff.ic.mining.ruleextraction

import br.uff.ic.extensions.deleteOnShutdown
import br.uff.ic.mining.featureselection.DataSet
import ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori
import java.nio.file.Files

class AprioriRuleExtractor(
        private val minimumSupport: Double,
        private val minimumConfidence: Double,
        private val minimumLift: Double
) : RuleExtractor {
    override fun extract(dataSet: DataSet): Set<Rule> {
        val apriori = AlgoApriori()
        val temp = Files.createTempFile("", "temp")
        temp.toFile().deleteOnShutdown()
        Files.write(temp, dataSet.toString().toByteArray())
        val itemSets = apriori.runAlgorithm(minimumSupport, temp.toAbsolutePath().toString(), null)
        val agrawal = AlgoAgrawalFaster94()
        val rules = agrawal.runAlgorithm(itemSets, null, apriori.databaseSize, minimumConfidence, minimumLift)
        rules.rules.map { rule ->
            rule.itemset2.map { consequent ->
                Rule(
                        premises = rule.itemset1.map { dataSet.header[it] },
                        consequent = dataSet.header[consequent],
                        confidence = rule.confidence,
                        lift = rule.lift,
                        support = (rule.absoluteSupport.toDouble() / apriori.databaseSize.toDouble()),
                        coverage = rule.coverage.toDouble()
                )
            }
        }
    }
}