package aoc.days

import util.FileLoader

val day2 = fun() {

    val lines = FileLoader.loadFileAsLines("/day2_input.txt")
    val total = lines
        .map { it.split(" ") }
        .map { Pair(it[0].toEnemyChoiceEnum(), it[1].toMyChoiceEnum()) }
        .sumOf { (enemyPlay, myPlay) -> (myPlay fight enemyPlay).resultPoints + myPlay.choicePoints }

    println(total)

    val newRulesTotal = lines
        .map { it.split(" ") }
        .map { Pair(it[0].toEnemyChoiceEnum(), it[1].toResult()) }
        .sumOf { (enemyPlay, wantedResult) -> wantedResult.resultPoints + (enemyPlay getWhatShouldIPlaySoResultIs wantedResult).choicePoints }

    println(newRulesTotal)
}

enum class Result(val resultPoints: Int, val representedBy: String) {
    WIN(6, "Z"),
    DRAW(3, "Y"),
    LOOSE(0, "X");
}

enum class MyChoice(val representedBy: String, val choicePoints: Int) {
    ROCK("X", 1),
    PAPER("Y", 2),
    SCISSORS("Z", 3);

    infix fun fight(enemy: EnemyChoice): Result {
        return when (this) {
            ROCK -> when (enemy) {
                EnemyChoice.ROCK -> Result.DRAW
                EnemyChoice.PAPER -> Result.LOOSE
                EnemyChoice.SCISSORS -> Result.WIN
            }

            PAPER -> when (enemy) {
                EnemyChoice.ROCK -> Result.WIN
                EnemyChoice.PAPER -> Result.DRAW
                EnemyChoice.SCISSORS -> Result.LOOSE
            }

            SCISSORS -> when (enemy) {
                EnemyChoice.ROCK -> Result.LOOSE
                EnemyChoice.PAPER -> Result.WIN
                EnemyChoice.SCISSORS -> Result.DRAW
            }
        }
    }
}

enum class EnemyChoice(val representedBy: String) {
    ROCK("A"),
    PAPER("B"),
    SCISSORS("C");

    infix fun getWhatShouldIPlaySoResultIs(result: Result): MyChoice {
        return when (this) {
            ROCK -> when (result) {
                Result.WIN -> MyChoice.PAPER
                Result.DRAW -> MyChoice.ROCK
                Result.LOOSE -> MyChoice.SCISSORS
            }

            PAPER -> when (result) {
                Result.WIN -> MyChoice.SCISSORS
                Result.DRAW -> MyChoice.PAPER
                Result.LOOSE -> MyChoice.ROCK
            }

            SCISSORS -> when (result) {
                Result.WIN -> MyChoice.ROCK
                Result.DRAW -> MyChoice.SCISSORS
                Result.LOOSE -> MyChoice.PAPER
            }
        }
    }
}

private fun String.toMyChoiceEnum() = MyChoice.values().first { it.representedBy == this }
private fun String.toEnemyChoiceEnum() = EnemyChoice.values().first { it.representedBy == this }
private fun String.toResult() = Result.values().first { it.representedBy == this }

