package br.uff.ic.extensions

fun <T> orNull(call: () -> T): T? {
    return try {
        call()
    } catch (_: Exception) {
        null
    }
}