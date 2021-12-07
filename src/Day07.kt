import kotlin.math.abs

fun main() {
    fun parseInput(input: List<String>) = input.first().split(',').map { it.toInt() }.toTypedArray()
    fun evalFuel(input: List<String>, fuelFn: (crab: Int, pos: Int) -> Int): Int {
        val crabs = parseInput(input)
        return (1..crabs.size).minOf { i -> crabs.sumOf { c -> fuelFn(c, i) } }
    }

    // https://adventofcode.com/2021/day/7
    // The input is a horizontal position of each crab.
    // Crab submarines have limited fuel, so you need to find a way to make all of their horizontal positions match
    // while requiring them to spend as little fuel as possible.
    // How much fuel must they spend to align to that position?
    fun part1(input: List<String>): Int {
        return evalFuel(input) { c, i -> abs(c - i) }
    }

    // Each change of 1 step in horizontal position costs 1 more unit of fuel than the last:
    // the first step costs 1, the second step costs 2, the third step costs 3, and so on.
    // Determine the horizontal position that the crabs can align to using the least fuel possible.
    // How much fuel must they spend to align to that position?
    fun part2(input: List<String>): Int {
        return evalFuel(input) { c, i -> (1..abs(c - i)).sum() }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
