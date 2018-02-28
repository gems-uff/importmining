package br.uff.ic.pipelines

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

@Suppress("JoinDeclarationAndAssignment")
/**
 * Class responsible for saving the states on json files
 * */
class JsonBucket(directory: String) {
    private val directoryPath: Path
    private val parser: ObjectMapper

    init {
        parser = ObjectMapper().registerKotlinModule()
        directoryPath = File(directory).let { FileSystems.getDefault().getPath(it.toString(), "$it-state.json") }
    }

    /**
     * does the file exists?
     * */
    fun contains(name: String): Boolean {
        return Files.exists(getPath(name))
    }


    /**
     * save me the state under this name, please
     * */
    fun save(name: String, state: Any) {
        Files.newBufferedWriter(getPath(name)).use { writer ->
            parser.writeValue(writer, state)
        }
    }

    /**
     * load me this item of this class, please
     * */
    fun <T : Any> load(name: String, clazz: KClass<T>): T {
        Files.newBufferedReader(getPath(name)).use { reader ->
            return parser.readValue(reader, clazz.java)
        }
    }

    private fun getPath(name : String) : Path =
        FileSystems.getDefault().getPath(directoryPath.toString(), "$name.json")
}