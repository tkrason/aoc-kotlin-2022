package aoc.days

import util.FileLoader

val day1 = fun() {

    val input = FileLoader.loadFileAsText("/day1_input.txt")

    val eachElfCaloriesList = input
        .split("${System.lineSeparator()}${System.lineSeparator()}")

    val elfCalorieSums = eachElfCaloriesList.map {
        it.sumCaloriesForElf()
    }

    val topCalorieElf = elfCalorieSums.maxOf { it }
    println("Most calories carried by any elf: $topCalorieElf")

    val topThreeCalorieElfs = elfCalorieSums.sortedDescending().take(3).sum()
    print("Sum of top 3 calorie carries is: $topThreeCalorieElfs")
}

private fun String.sumCaloriesForElf() = this
    .split(System.lineSeparator())
    .sumOf { it.toInt() }
