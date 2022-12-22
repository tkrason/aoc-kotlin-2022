package aoc

import aoc.days.day1
import aoc.days.day2
import aoc.days.day3

enum class Solution(val function: () -> Unit) {
    DAY_1(day1),
    DAY_2(day2),
    DAY_3(day3),
}