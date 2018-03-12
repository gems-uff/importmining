package br.uff.ic.domain

import br.uff.ic.mining.Rule

class Coupling(val clazz : String, val rulesInvolved : Collection<Rule>)