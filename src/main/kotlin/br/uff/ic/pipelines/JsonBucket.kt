package br.uff.ic.pipelines

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

/**
 * Class responsible for saving the states on json files
 * */
class JsonBucket(
        directory: String,
        private val parser: ObjectMapper = ObjectMapper().registerKotlinModule()
) {
    private val directory: Path = File(directory).toPath().toAbsolutePath()

    /**
     * does the file exists?
     * */
    fun contains(name: String): Boolean {
        return Files.exists(name.toPath())
    }


    /**
     * save me the state under this name, please
     * */
    fun save(name: String, state: Any) {
        Files.newBufferedWriter(name.toPath()).use { writer ->
            parser.writeValue(writer, state)
        }
    }

    /**
     * load me this item of this class, please
     * */
    fun <T : Any> load(name: String, clazz: KClass<T>): T {
        Files.newBufferedReader(name.toPath()).use { reader ->
            return parser.readValue(reader, clazz.java)
        }
    }

    private fun String.toPath() = FileSystems.getDefault().getPath(directory.toString(), "$this-state.json")
}
