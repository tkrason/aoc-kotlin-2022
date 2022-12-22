package aoc.days

import util.FileLoader

val day6 = fun() {

    val text = FileLoader.loadFileAsText("/day6_input.txt")

    val startOfPacket = text.findPacketStart()
    println(startOfPacket)
    val startOfMessage = text.findMessageStart()
    println(startOfMessage)
}

fun String.findPacketStart() = findPoint(4)
fun String.findMessageStart() = findPoint(14)

fun String.findPoint(size: Int) = this
    .asSequence()
    .windowed(size)
    .indexOfFirst { it.toSet().size == size } + size
