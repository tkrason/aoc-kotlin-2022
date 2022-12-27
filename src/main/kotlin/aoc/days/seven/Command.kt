package aoc.days.seven

sealed interface Command

data class Cd(
    val newTarget: String
) : Command

data class Ls(
    val files: List<File>
) : Command