package aoc.days

import util.FileLoader
import java.math.BigDecimal

val day11 = fun() {

    val text = FileLoader.loadFileAsText("/day11_input.txt")

    val monkeys = text.parseMonkeys()
    monkeys.forEach { it.initTrueFalseMonkeys(monkeys) }

    repeat(20) { monkeys.simulateRound() }

    val topTwoThrowingMonkeys = monkeys
        .sortedByDescending { it.inspectionCounter }
        .take(2)
        .map { it.inspectionCounter }

    println(topTwoThrowingMonkeys[0] * topTwoThrowingMonkeys[1])

    //Part 2
    val newMonkeys = text.parseMonkeys()
    newMonkeys.forEach { it.initTrueFalseMonkeys(newMonkeys) }

    repeat(10_000) { newMonkeys.simulateRound(applyWorryLevelDivision = false) }

    val newTopTwoThrowingMonkeys = newMonkeys
        .sortedByDescending { it.inspectionCounter }
        .take(2)
        .map { it.inspectionCounter }

    println(newTopTwoThrowingMonkeys[0].toBigDecimal() * newTopTwoThrowingMonkeys[1].toBigDecimal())
}

private fun String.parseMonkeys() = this
    .split("Monkey")
    .drop(1) // fix for first value being empty
    .map { it.split(System.lineSeparator()) }
    .mapIndexed { index, splitLine ->
        Monkey(
            id = index,
            items = splitLine[1].parseToItems(),
            changeWorryLevel = splitLine[2].parseToOperation(),
            divisionRemainder = splitLine[3].parseToTest(),
            whenTrueThrowTo = splitLine[4].parseToMonkeyNumber(),
            whenFalseThrowTo = splitLine[5].parseToMonkeyNumber()
        )
    }

private fun String.parseToItems() = this
    .replace("Starting items: ", "")
    .split(", ")
    .map { it.trim().toInt().toBigDecimal() }
    .toMutableList()

private fun String.parseToOperation() = this
    .replace("Operation: ", "")
    .split(" ")
    .let { split ->
        // Assuming null means that the last parameter is "old"
        when(split[6].toIntOrNull()) {
            null -> when (split[5]) {
                "*" -> { old: BigDecimal -> old * old }
                "+" -> { old: BigDecimal -> old + old }
                else -> error("not supported operation!")
            }
            else -> when (split[5]) {
                "*" -> { old: BigDecimal -> old * split[6].toBigDecimal() }
                "+" -> { old: BigDecimal -> old + split[6].toBigDecimal() }
                else -> error("not supported operation!")
            }
        }
    }

private fun String.parseToTest() = this
    .split("divisible by ")
    .let { split -> { value: BigDecimal -> value % split[1].toBigDecimal() } }

private fun String.parseToMonkeyNumber() = this
    .split("monkey ")
    .last()
    .toInt()

data class Monkey(
    val id: Int,
    val items: MutableList<BigDecimal>,
    val changeWorryLevel: (BigDecimal) -> BigDecimal,
    val divisionRemainder: (BigDecimal) -> BigDecimal,
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

    fun inspectAndThrowAll(worryIsDividing : Boolean) {
        while (items.isNotEmpty()) {
            inspectionCounter++
            when(worryIsDividing) {
                true -> inspectItemAndThrow(items.first())
                false -> inspectItemAndThrowWithoutWorryDivision(items.first())
            }
        }
    }

    private fun inspectItemAndThrow(worryLevel: BigDecimal) {

        val newWorryLevel = changeWorryLevel(worryLevel)
        val afterMonkeyGetsBored = (newWorryLevel.toDouble() / 3).toInt().toBigDecimal()

        when(divisionRemainder(afterMonkeyGetsBored) == BigDecimal.ZERO) {
            true -> trueThrowMonkey.catch(afterMonkeyGetsBored)
            false -> falseThrowMonkey.catch(afterMonkeyGetsBored)
        }

        // Got thrown, can delete now
        items.removeFirst()
    }

    private val leastCommonMultiple = BigDecimal("9699690")
    private fun inspectItemAndThrowWithoutWorryDivision(worryLevel: BigDecimal) {
        val newWorryLevel = changeWorryLevel(worryLevel)

        when(divisionRemainder(newWorryLevel)) {
            BigDecimal.ZERO -> trueThrowMonkey.catch(newWorryLevel % leastCommonMultiple)
            else -> falseThrowMonkey.catch(newWorryLevel % leastCommonMultiple)
        }

        // Got thrown, can delete now
        items.removeFirst()
    }

    private fun catch(itemWithWorryLevel: BigDecimal) {
        items.add(itemWithWorryLevel)
    }
}

fun List<Monkey>.simulateRound(applyWorryLevelDivision : Boolean = true) {
    this.forEach { it.inspectAndThrowAll(applyWorryLevelDivision) }
}
