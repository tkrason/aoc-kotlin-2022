package aoc.days

import util.FileLoader

val day3 = fun() {

    val backpacks = FileLoader.loadFileAsLines("/day3_input.txt")

    val sumOfPriorities = backpacks
        .map { backpack -> backpack.splitInMiddle() }
        .map { backpackParts -> backpackParts.map { it.toSet() } }
        .map { (it[0] intersect it[1]).first() }
        .sumOf { it.getPriority() }

    println(sumOfPriorities)

    val sumOfBadgesPriorities = backpacks
        .windowed(size = 3, step = 3)
        .map { elfGroup -> elfGroup.map { backpack -> backpack.toSet() } }
        .map { (it[0] intersect it[1] intersect it[2]).first() }
        .sumOf { it.getPriority() }

    println(sumOfBadgesPriorities)
}

fun String.splitInMiddle() = listOf(
    substring(0, length / 2), substring(length / 2, length)
)

const val utf16UppercaseA = 65
const val utf16LowercaseA = 97
fun Char.getPriority() = when (isUpperCase()) {
    false -> 1 + (code - utf16LowercaseA)
    true -> 27 + (code - utf16UppercaseA)
}
