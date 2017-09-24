package br.uff.ic.pipelines

inline fun <reified T : Any> State.load(name: String, noinline create: () -> T): T {
    return load(name, T::class, create)
}