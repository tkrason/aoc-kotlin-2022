package aoc.days

import util.FileLoader

val day10 = fun() {

    val lines = FileLoader.loadFileAsLines("/day10_input.txt")

    val instructions = lines
        .map { it.split(" ") }
        .map {
            when (it[0]) {
                "addx" -> Addx(it[1].toInt())
                "noop" -> Noop()
                else -> error("Unknown instruction found!")
            }
        }

    val cpu = Cpu()
    cpu.runInstructions(instructions)
    println(cpu.getSignalStrength())
    cpu.printCtrScreen()
}

class Cpu {

    private var cycle: Int = 1
    var registerX: Int = 1
    private val collectRegisterStateAtCycle = listOf(20, 60, 100, 140, 180, 220)
    private var signalStrengthCollector = mutableListOf<Int>()
    private var ctrScreen = mutableListOf<String>()

    fun runInstructions(instructions: List<Instruction>) {
        instructions.forEach { it.work(this) }
    }

    fun tick() {
        if (cycle in collectRegisterStateAtCycle) { signalStrengthCollector.add(cycle * registerX) }
        drawPixelOnCtr()
        cycle++
    }

    fun getSignalStrength() = signalStrengthCollector.sum()

    val CRT_LIT = "#"
    val CRT_UNLIT = "."

    private fun drawPixelOnCtr() {
        val printingInPixel = (cycle - 1) % 40
        val spriteCoversPositions = registerX - 1..registerX + 1
        when (printingInPixel in spriteCoversPositions) {
            true -> ctrScreen.add(CRT_LIT)
            false -> ctrScreen.add(CRT_UNLIT)
        }
    }

    fun printCtrScreen() = ctrScreen
        .chunked(40)
        .forEach { println(it.joinToString(separator = "")) }

}

interface Instruction {
    fun work(cpu: Cpu)
}

class Noop : Instruction {
    override fun work(cpu: Cpu) {
        cpu.tick()
    }
}

class Addx(
    private val value: Int
) : Instruction {
    override fun work(cpu: Cpu) {
        cpu.tick()
        cpu.tick()
        cpu.registerX += value
    }
}