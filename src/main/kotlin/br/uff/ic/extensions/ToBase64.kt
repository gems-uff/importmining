package br.uff.ic.extensions

import java.util.*


fun Any.toBase64(): String {
    return Base64.getEncoder().encodeToString(this.hashCode().toString().toByteArray())
}