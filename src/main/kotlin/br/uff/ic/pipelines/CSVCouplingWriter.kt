package br.uff.ic.pipelines

import au.com.bytecode.opencsv.CSVWriter
import br.uff.ic.domain.CouplingInfo
import java.io.File
import java.io.FileWriter
import java.io.IOException

class CSVCouplingWriter(val path : String) {
    val writer : CSVWriter = CSVWriter(FileWriter(File(path)), ',', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_SEPARATOR)
    val header : Array<String> = arrayOf("Class", "n", "SCC", "SOC")

    private fun writeRecord(record : CouplingInfo){
        writer.writeNext(arrayOf(record.clazz,
                record.n.toString(),
                record.scc.joinToString(separator = ";"),
                record.soc.toString()))
    }

    fun writeData(set : Collection<CouplingInfo>){
        try{
            writer.writeNext(header)
            set.forEach { writeRecord(it) }
            writer.close()
        } catch (e : IOException){
            e.printStackTrace()
        }
    }
}