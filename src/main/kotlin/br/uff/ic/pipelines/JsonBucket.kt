package br.uff.ic.pipelines

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

class JsonBucket(
        directory: String,
        private val parser: ObjectMapper
) : Bucket {
    private val directory: Path = File(directory).toPath().toAbsolutePath()
    override fun contains(name: String): Boolean {
        return Files.exists(name.toPath())
    }

    override fun save(name: String, state: Any) {
        Files.newBufferedWriter(name.toPath()).use { writer ->
            parser.writeValue(writer, state)
        }
    }

    override fun <T : Any> load(name: String, clazz: KClass<T>): T {
        Files.newBufferedReader(name.toPath()).use { reader ->
            return parser.readValue(reader, clazz.java)
        }
    }

    private fun String.toPath() = FileSystems.getDefault().getPath(directory.toString(), "$this.state")
}
