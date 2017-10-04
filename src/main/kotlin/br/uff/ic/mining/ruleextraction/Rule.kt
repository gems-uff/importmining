package br.uff.ic.mining.ruleextraction

data class Rule(
    val premise: List<String>,
    val consequence: List<String>,
    val confidence: Double,
    val lift: Double,
    val leverage: Double,
    val support: Double,
    val conviction: Double
)