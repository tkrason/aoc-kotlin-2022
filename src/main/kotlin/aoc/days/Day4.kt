package aoc.days

import util.FileLoader

val day4 = fun() {

    val lines = FileLoader.loadFileAsLines("/day4_input.txt")

    val result = lines
        .map { it.split(",") }
        .map { workRanges -> workRanges.map { it.constructIntRange() } }
        .map { workRanges -> workRanges[0] containsFully workRanges[1] || workRanges[1] containsFully workRanges[0] }
        .numberOfTrues()

    println(result)

    val overlapAtLeastOnOne = lines
        .map { it.split(",") }
        .map { workRanges -> workRanges.map { it.constructIntRange() } }
        .map { workRanges -> workRanges[0] isAnyOverlapWith workRanges[1] }
        .numberOfTrues()

    println(overlapAtLeastOnOne)
}

fun String.constructIntRange() = this
    .split("-")
    .map { it.toInt() }
    .let { it[0]..it[1] }

infix fun IntRange.containsFully(other: IntRange) =
    this.first <= other.first && this.last >= other.last

infix fun IntRange.isAnyOverlapWith(other: IntRange) =
    (this intersect other).isNotEmpty()

fun List<Boolean>.numberOfTrues() = this.filter { it }.size