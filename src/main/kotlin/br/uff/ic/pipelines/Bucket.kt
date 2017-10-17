package br.uff.ic.pipelines

import kotlin.reflect.KClass

interface Bucket {
    operator fun contains(name: String): Boolean

    fun save(name: String, state: Any)

    fun <T : Any> load(name: String, clazz: KClass<T>): T
}