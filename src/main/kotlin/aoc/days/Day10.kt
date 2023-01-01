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

    println(cpu.signalStrengthCollector.sum())
}

class Cpu {

    private var cycle: Int = 1
    var registerX: Int = 1
    val collectRegisterStateAtCycle = listOf(20, 60, 100, 140, 180, 220)
    var signalStrengthCollector = mutableListOf<Int>()

    fun runInstructions(instructions: List<Instruction>) {
        instructions.forEach { it.work(this) }
    }

    fun tick() {
        if(cycle in collectRegisterStateAtCycle) { signalStrengthCollector.add(cycle * registerX)}
        cycle++
    }
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