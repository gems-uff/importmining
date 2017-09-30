package br.uff.ic.pipelines

import br.uff.ic.mining.Rule

abstract class Pipeline<in In : Any, out Out : Any>(
        override val checkpoints: Bucket
) : State {
    abstract fun execute(input: In): List<Rule>
}