package br.uff.ic.mining

data class FrequentSet(
    val items: Set<String>,
    val support: Double,
    val instances: List<String>
)