package br.uff.ic.collector

interface ImportCollector {
    companion object {
        fun new(spec: Map<String, String>) : ImportCollector {
            val output = spec["output"]
            if (output == null) {
                error("'Output' must be specified")
            }
            val channel = OutputChannel.new(output)
            return ExplicitImportCollector(channel)


        }
    }

    suspend fun collect(project: Project)
}