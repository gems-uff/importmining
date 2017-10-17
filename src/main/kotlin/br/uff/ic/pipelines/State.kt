package br.uff.ic.pipelines

import kotlin.reflect.KClass

interface State {
    val checkpoints: Bucket
    fun saveState(name: String, state: Any) {
        if (name !in checkpoints) {
            checkpoints.save(name, state)
        }
    }

    fun <T : Any> load(name: String, clazz: KClass<T>, create: () -> T): T {
        return when (name) {
            in checkpoints -> checkpoints.load(name, clazz)
            else -> {
                val state = create()
                saveState(name, state)
                state
            }
        }
    }
}
