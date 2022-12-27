package aoc.days.seven

import java.math.BigDecimal

data class Directory(
    val parent: Directory?,
    val name: String,
    var dirs: MutableMap<String, Directory> = mutableMapOf(),
    var files: MutableList<File> = mutableListOf()
) {
    fun getOrCreateDirectory(name: String): Directory {
        return dirs[name] ?: Directory(parent = this, name = name).also { dirs[name] = it }
    }

    fun addFile(file: File) = files.add(file)

    fun sizeOf(): BigDecimal = files.getTotalSize().plus(dirs.values.sumOf { it.sizeOf() })

    override fun toString(): String {
        return "Directory(name='$name')"
    }

}

fun Directory.simulateCommands(commands: List<Command>) = commands.fold(this) { node, command ->
    when (command) {
        is Cd -> when (val moveTo = command.newTarget) {
            ".." -> node.parent!!
            else -> node.getOrCreateDirectory(moveTo)
        }

        is Ls -> {
            command.files.forEach { node.addFile(it) }
            node
        }
    }
}

fun Directory.getTotalSumOfSizesForDirectoriesMatching(predicate: (Directory) -> Boolean): BigDecimal {
    var total = BigDecimal.ZERO
    if (predicate(this)) {
        total += this.sizeOf()
    }
    total += this.dirs.values.sumOf { it.getTotalSumOfSizesForDirectoriesMatching(predicate) }
    return total
}

@OptIn(ExperimentalStdlibApi::class)
fun Directory.getListOfDirectorySizes(): List<BigDecimal> {
    return buildList {
        add(this@getListOfDirectorySizes.sizeOf())
        addAll(this@getListOfDirectorySizes.dirs.values.flatMap { it.getListOfDirectorySizes() })
    }
}

