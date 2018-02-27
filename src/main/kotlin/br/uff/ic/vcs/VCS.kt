package br.uff.ic.vcs

import br.uff.ic.domain.Project
import java.io.File


interface VCS {
    fun clone(uri: String) : Project
}