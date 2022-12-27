package aoc.days.seven

import java.math.BigDecimal

data class File(
    val name: String,
    val size: BigDecimal
)

fun List<File>.getTotalSize() = this.sumOf { it.size }