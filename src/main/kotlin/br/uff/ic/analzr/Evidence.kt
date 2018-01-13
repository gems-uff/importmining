package br.uff.ic.analzr

import br.uff.ic.mining.Rule

data class Evidence(val anomalyName : String, val rulesRelated : Iterable<Rule>, val classesRelated : Iterable<String>)