package aoc

import aoc.days.*
import aoc.days.seven.day7

enum class Solution(val function: () -> Unit) {
    DAY_1(day1),
    DAY_2(day2),
    DAY_3(day3),
    DAY_4(day4),
    DAY_5(day5),
    DAY_6(day6),
    DAY_7(day7),
    DAY_8(day8),
    DAY_9(day9),
    DAY_10(day10),
}