package br.uff.ic.extensions


fun <T> MutableList<out T>.toStringSet(): Set<String> {
    return this.map { it.toString() }.toSet()
}

