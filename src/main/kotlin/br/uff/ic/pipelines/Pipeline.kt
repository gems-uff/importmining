package br.uff.ic.pipelines

abstract class Pipeline<in In : Any, out Out : Any>(
        override val checkpoints: Bucket
) : State {
    abstract fun execute(input: In): Out
}