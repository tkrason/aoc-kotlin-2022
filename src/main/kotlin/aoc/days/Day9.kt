package aoc.days

import util.FileLoader
import kotlin.math.abs

@OptIn(ExperimentalStdlibApi::class)
val day9 = fun() {

    val lines = FileLoader.loadFileAsLines("/day9_input.txt")

    val moves = lines.map {
        val split = it.split(" ")
        split[0].toDirection() to split[1].toInt()
    }

    val head = RopeKnot(0, 0)
    val tail = RopeKnot(0, 0)
    tail willFollow head

    moves.forEach { move ->
        head.executeMove(move)
    }

    println(tail.visited.size)

    // Part 2
    val knots = buildList { repeat(10) { add(RopeKnot(0, 0)) } }

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

data class RopeKnot(
    var x: Int,
    var y: Int,
    val followers: MutableList<RopeKnot> = mutableListOf(),
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
) {


    fun executeMove(pair: Pair<RopeDirection, Int>) {
        repeat(pair.second) {
            moveInDirection(pair.first)
        }
    }

    private fun moveInDirection(direction: RopeDirection) {
        saveLocation()
        when (direction) {
            RopeDirection.UP -> y += 1
            RopeDirection.DOWN -> y -= 1
            RopeDirection.RIGHT -> x += 1
            RopeDirection.LEFT -> x -= 1
        }
        saveLocation()
        moveFollowersIfNeeded()
    }

    private fun saveLocation() = visited.add(x to y)

    private fun moveFollowersIfNeeded() =
        followers.forEach { follower -> follower.simulateKnotMovementWithParentAt(x, y) }

    private fun simulateKnotMovementWithParentAt(parentX: Int, parentY: Int) {
        saveLocation()

        val absoluteX = abs(parentX - x)
        val absoluteY = abs(parentY - y)

        if (knotIsNotTouchingParent(absoluteX, absoluteY)) {

            val moveInX = when {
                parentX - x > 0 -> 1
                parentX - x < 0 -> -1
                else -> 0
            }

            val moveInY = when {
                parentY - y > 0 -> 1
                parentY - y < 0 -> -1
                else -> 0
            }

            x += moveInX
            y += moveInY

            saveLocation()
            moveFollowersIfNeeded()
        }
    }
}

private infix fun RopeKnot.willFollow(rope: RopeKnot) {
    rope.followers.add(this)
}

private fun knotIsNotTouchingParent(absoluteX: Int, absoluteY: Int) = absoluteX > 1 || absoluteY > 1