package br.uff.ic.extensions

import br.uff.ic.pipelines.State

inline fun <reified T : Any> State.load(name: String, noinline create: () -> T): T {
    return load(name, T::class, create)
}