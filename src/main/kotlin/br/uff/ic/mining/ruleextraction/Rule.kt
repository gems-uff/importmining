package br.uff.ic.mining.ruleextraction

data class Rule(
        val premises: List<String>,
        val consequent: String,
        val support: Double,
        val coverage: Double,
        val confidence: Double,
        val conviction: Double,
        val lift: Double
)