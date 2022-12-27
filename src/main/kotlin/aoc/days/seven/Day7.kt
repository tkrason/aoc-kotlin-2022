package aoc.days.seven

import util.FileLoader
import java.math.BigDecimal


val day7 = fun() {

    val text = FileLoader.loadFileAsText("/day7_input.txt")

    val commandsUnparsed = text
        .split("$ ")
        .let { it.subList(1, it.size) } // Fix for first element being empty string

    val commands = commandsUnparsed
        .map { it.parseIntoCommandAndPossibleConsoleOutput() }
        .map {
            val command = it.first()
            when {
                command.startsWith("cd") -> it.toCdCommand()
                command.startsWith("ls") -> it.toLsCommand()
                else -> error("Unknown command!")
            }
        }.drop(1) // Drop first "cd /" because we create root by default

    val root = Directory(parent = null, name = "/")
    root.simulateCommands(commands)

    // Task 1
    println(root.getTotalSumOfSizesForDirectoriesMatching { it.sizeOf() <= BigDecimal(100_000) })

    // Task 2
    val systemSize = BigDecimal(70_000_000)
    val currentSize = root.sizeOf()
    val spaceLeft = systemSize - currentSize
    val sizeNeededForUpdate = BigDecimal(30_000_000)
    println(root
        .getListOfDirectorySizes()
        .sorted()
        .first { deleteCandidate -> spaceLeft + deleteCandidate >= sizeNeededForUpdate }
    )
}

private fun String.parseIntoCommandAndPossibleConsoleOutput() = this
    .split(System.lineSeparator())
    .filter { it.isNotBlank() }

private fun List<String>.toCdCommand() = this[0]
    .split(" ") // e.g. -> [cd, /]
    .let {
        val cdGoesTo = it[1]
        Cd(newTarget = cdGoesTo)
    }

private fun List<String>.toLsCommand() = this
    .subList(1, this.size)
    .let { lsOutputLine ->
        // for file -> 123500 hello.txt
        val files = lsOutputLine
            .map { it.split(" ") }
            .filter { !it[0].contains("dir") }
            .map { File(name = it[1], size = BigDecimal(it[0])) }

        Ls(files = files)
    }





