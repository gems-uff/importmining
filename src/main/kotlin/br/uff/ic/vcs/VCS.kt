package br.uff.ic.vcs

import java.io.File


interface VCS {
    fun clone(uri: String, directory: File)
}