package util

object FileLoader {
    fun loadFile(fileName: String) = {}::class.java.getResource(fileName)!!.readText()
}