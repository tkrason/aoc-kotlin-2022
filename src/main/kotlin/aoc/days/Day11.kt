package aoc.days

import util.FileLoader

val day11 = fun() {

    val text = FileLoader.loadFileAsText("/day11_input.txt")

    val monkeys = text
        .split("Monkey")
        .drop(1) // fix for first value being empty
        .map { it.split(System.lineSeparator()) }
        .mapIndexed { index, splitLine ->
            Monkey(
                id = index,
                items = splitLine[1].parseToItems(),
                operation = splitLine[2].parseToOperation(),
                test = splitLine[3].parseToTest(),
                whenTrueThrowTo = splitLine[4].parseToMonkeyNumber(),
                whenFalseThrowTo = splitLine[5].parseToMonkeyNumber()
            )
        }
    monkeys.forEach { it.initTrueFalseMonkeys(monkeys) }

    repeat(20) { monkeys.simulateRound() }

    val topTwoThrowingMonkeys = monkeys
        .sortedByDescending { it.inspectionCounter }
        .take(2)
        .map { it.inspectionCounter }

    println(topTwoThrowingMonkeys[0] * topTwoThrowingMonkeys[1])
}

private fun String.parseToItems() = this
    .replace("Starting items: ", "")
    .split(", ")
    .map { it.trim().toInt() }
    .toMutableList()

private fun String.parseToOperation() = this
    .replace("Operation: ", "")
    .split(" ")
    .let { split ->
        // Assuming null means that the last parameter is "old"
        when(split[6].toIntOrNull()) {
            null -> when (split[5]) {
                "*" -> { old: Int -> old * old }
                "+" -> { old: Int -> old + old }
                else -> error("not supported operation!")
            }
            else -> when (split[5]) {
                "*" -> { old: Int -> old * split[6].toInt() }
                "+" -> { old: Int -> old + split[6].toInt() }
                else -> error("not supported operation!")
            }
        }
    }

private fun String.parseToTest() = this
    .split("divisible by ")
    .let { split -> { value: Int -> value % split[1].toInt() == 0 } }

private fun String.parseToMonkeyNumber() = this
    .split("monkey ")
    .last()
    .toInt()

data class Monkey(
    val id: Int,
    val items: MutableList<Int>,
    val operation: (Int) -> Int,
    val test: (Int) -> Boolean,
    val whenTrueThrowTo: Int,
    val whenFalseThrowTo: Int
) {
    private lateinit var trueThrowMonkey : Monkey
    private lateinit var falseThrowMonkey : Monkey
    var inspectionCounter = 0

    fun initTrueFalseMonkeys(list: List<Monkey>) {
        trueThrowMonkey = list[whenTrueThrowTo]
        falseThrowMonkey = list[whenFalseThrowTo]
    }

    fun inspectAndThrowAll() {
        while (items.isNotEmpty()) {
            inspectionCounter++
            inspectItemAndThrow(items.first())
        }
    }

    private fun inspectItemAndThrow(worryLevel: Int) {

        val newWorryLevel = operation(worryLevel)
        val afterMonkeyGetsBored = (newWorryLevel.toDouble() / 3).toInt()

        when(test(afterMonkeyGetsBored)) {
            true -> trueThrowMonkey.catch(afterMonkeyGetsBored)
            false -> falseThrowMonkey.catch(afterMonkeyGetsBored)
        }

        // Got thrown, can delete now
        items.removeFirst()
    }

    private fun catch(itemWithWorryLevel: Int) {
        items.add(itemWithWorryLevel)
    }
}

fun List<Monkey>.simulateRound() {
    this.forEach { it.inspectAndThrowAll() }
}
