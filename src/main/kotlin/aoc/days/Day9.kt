package aoc.days

import util.FileLoader
import kotlin.math.abs

val day9 = fun() {

    val lines = FileLoader.loadFileAsLines("/day9_test_input.txt")

    val moves = lines.map {
        val split = it.split(" ")
        split[0].toDirection() to split[1].toInt()
    }

    val head = RopeEnd(0, 0)
    val tail = RopeEnd(0, 0)
    tail willFollow head

    moves.forEach { move->
        head.executeMove(move)
    }

    println(tail.visited.size)

    // Part 2
    val knots = buildList { repeat(10) { add(RopeEnd(0,0)) } }

    knots
        .windowed(2, partialWindows = false)
        .forEach { it[1] willFollow it[0] }

    moves.forEach { move ->
        knots[0].executeMove(move)
    }

    println(knots.last().visited.size)
}

enum class RopeDirection(val representedBy: String) {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R");
}

private fun String.toDirection() = RopeDirection.values().find { it.representedBy == this }!!

data class RopeEnd(
    var x: Int,
    var y: Int,
    val followers: MutableList<RopeEnd> = mutableListOf(),
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
) {


    fun executeMove(pair: Pair<RopeDirection, Int>) {
        repeat(pair.second) {
            moveInDirection(pair.first)
        }
    }

    private fun moveInDirection(direction: RopeDirection) {
        saveLocation()
        val currentX = x
        val currentY = y
        when(direction) {
            RopeDirection.UP -> y += 1
            RopeDirection.DOWN -> y -= 1
            RopeDirection.RIGHT -> x += 1
            RopeDirection.LEFT -> x -= 1
        }
        saveLocation()
        followers.forEach { it.moveIfNeeded(currentX, currentY, x, y) }
    }

    private fun saveLocation() = visited.add(x to y)

    private fun moveIfNeeded(previousParentX: Int, previousParentY: Int, parentX: Int, parentY: Int) {
        saveLocation()
        val currentX = x
        val currentY = y

        val absX = abs(parentX - x)
        val absY = abs(parentY - y)

        /*
            0 0 0 0 0
            0 0 0 p p
            0 0 x 0 0
            0 0 0 0 0
            0 0 0 0 0
         */
        if(absX > 1 || absY > 1) {
            x = previousParentX
            y = previousParentY
            saveLocation()
            followers.forEach { it.moveIfNeeded(currentX, currentY, x, y) }
        }
    }
}

private infix fun RopeEnd.willFollow(rope: RopeEnd) {
    rope.followers.add(this)
}