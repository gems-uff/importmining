package br.uff.ic.domain

import br.uff.ic.mining.Itemset

/**
 * scc(c, n) = { ci | ci != c && c é acoplada em n à ci}
 * soc(c, n) = sum (ci in scc(c, n)) |{t in T} ci in t && c in t|
 */
class CouplingInfo(val clazz : String, val n : Long, fps : Collection<Itemset>){

    val soc : Long
    val scc : Set<String>

    init {
        scc = calculateSCC(fps)
        soc = calculateSOC(fps)
    }

    private fun calculateSCC(fps: Collection<Itemset>) : Set<String> =
            fps.filter { it.frequency == n }
                .filter { it.items.contains(clazz) }
                .flatMap { it.items }
                .filter { it != clazz }
                .toSet()

    private fun calculateSOC(fps : Collection<Itemset>) = scc.fold(0L){
        acum, curClass -> fps.filter{ it.frequency == n}
                             .filter { it.items.containsAll(listOf(curClass, clazz)) }
                             .size + acum
    }
}