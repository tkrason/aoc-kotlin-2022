package aoc.days

import util.FileLoader
import kotlin.collections.ArrayDeque

val day5 = fun() {

    val lines = FileLoader.loadFileAsLines("/day5_input.txt")

    val craneOrders = lines
        .map { it.split(" ") }
        .map { CraneOrder(move = it[1].toInt(), from = it[3].toInt(), to = it[5].toInt()) }

    val crates = getInitialCrates()
    craneOrders.forEach { crates.executeOrderWithOldCrane(it) }
    println(crates.getTopCrateNames())

    val cratesWithNewCrane = getInitialCrates()
    craneOrders.forEach { cratesWithNewCrane.executeOrderWithNewCrane(it) }
    println(cratesWithNewCrane.getTopCrateNames())
}

//    [W] [V]     [P]
//    [B] [T]     [C] [B]     [G]
//    [G] [S]     [V] [H] [N] [T]
//    [Z] [B] [W] [J] [D] [M] [S]
//    [R] [C] [N] [N] [F] [W] [C]     [W]
//    [D] [F] [S] [M] [L] [T] [L] [Z] [Z]
//    [C] [W] [B] [G] [S] [V] [F] [D] [N]
//    [V] [G] [C] [Q] [T] [J] [P] [B] [M]
//    1   2   3   4   5   6   7   8   9
fun getInitialCrates() = listOf(
    ArrayDeque(listOf("V", "C", "D", "R", "Z", "G", "B", "W")),
    ArrayDeque(listOf("G", "W", "F", "C", "B", "S", "T", "V")),
    ArrayDeque(listOf("C", "B", "S", "N", "W")),
    ArrayDeque(listOf("Q", "G", "M", "N", "J", "V", "C", "P")),
    ArrayDeque(listOf("T", "S", "L", "F", "D", "H", "B")),
    ArrayDeque(listOf("J", "V", "T", "W", "M", "N")),
    ArrayDeque(listOf("P", "F", "L", "C", "S", "T", "G")),
    ArrayDeque(listOf("B", "D", "Z")),
    ArrayDeque(listOf("M", "N", "Z", "W")),
)

data class CraneOrder(
    val move: Int,
    val from: Int,
    val to: Int
)

fun List<ArrayDeque<String>>.executeOrderWithOldCrane(order: CraneOrder) {
    repeat(order.move) {
        val crate = this[order.from - 1].pop()
        this[order.to - 1].push(crate)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun List<ArrayDeque<String>>.executeOrderWithNewCrane(order: CraneOrder) {
    val crates = buildList {
        repeat(order.move) { add(this@executeOrderWithNewCrane[order.from - 1].pop()) }
    }

    crates.reversed().forEach { this[order.to - 1].push(it) }
}

fun List<ArrayDeque<String>>.getTopCrateNames() = this.joinToString(separator = "") { it.peak() }

fun ArrayDeque<String>.push(elem: String) = addLast(elem)
fun ArrayDeque<String>.pop() = removeLast()
fun ArrayDeque<String>.peak() = last()
