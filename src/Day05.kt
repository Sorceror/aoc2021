import java.util.function.Predicate
import kotlin.math.min
import kotlin.math.max

fun main() {
    data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
        fun isStraight() = x1 == x2 || y1 == y2
        fun coors(): Array<Pair<Int, Int>> {
            return when {
                x1 == x2 -> IntRange(min(y1, y2), max(y1, y2)).map { Pair(x1, it) }.toTypedArray()
                y1 == y2 -> IntRange(min(x1, x2), max(x1, x2)).map { Pair(it, y1) }.toTypedArray()
                else -> {
                    return if (x1 < x2) {
                        if (y1 < y2) {
                            (x1..x2).zip(y1..y2).toTypedArray()
                        } else {
                            (x1..x2).zip((y2..y1).reversed()).toTypedArray()
                        }
                    } else {
                        if (y1 < y2) {
                            (x2..x1).reversed().zip(y1..y2).toTypedArray()
                        } else {
                            (x2..x1).reversed().zip((y2..y1).reversed()).toTypedArray()
                        }
                    }
                }
            }
        }
    }

    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    fun parseInput(input: List<String>) = input
        .asSequence()
        .map { regex.find(it)?.destructured ?: error("parsing failed: $it") }
        .map { Line(it.component1().toInt(), it.component2().toInt(), it.component3().toInt(), it.component4().toInt()) }

    fun calculateOverlappingPoints(input: List<String>, p: Predicate<Line>) = parseInput(input)
        .filter { p.test(it) }
        .map { it.coors() }
        .fold(emptyList<Pair<Int, Int>>()) { acc, pairs -> acc + pairs }
        .groupBy { it }
        .mapValues { it.value.size }
        .filterValues { it >= 2 }
        .count()

    // https://adventofcode.com/2021/day/5
    // You come across a field of hydrothermal vents, they tend to form in lines (input).
    // Only consider horizontal and vertical lines.
    // At how many points do at least two lines overlap?
    fun part1(input: List<String>): Int {
        return calculateOverlappingPoints(input) { it.isStraight() }

    }

    // Also consider diagonal lines.
    // At how many points do at least two lines overlap?
    fun part2(input: List<String>): Int {
        return calculateOverlappingPoints(input) { true }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
