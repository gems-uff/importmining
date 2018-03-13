package br.uff.ic.domain

import br.uff.ic.mining.Rule

class Coupling(val clazz1 : String, val clazz2: String, val rulesInvolved : Collection<Rule>)