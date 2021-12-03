fun main() {
    data class Instruction(val c: Char, val index: Int)
    // https://adventofcode.com/2021/day/3
    // Each bit in the gamma rate can be determined by finding the most common bit in the corresponding position of all numbers in the diagnostic report.
    // The epsilon rate is calculated in a similar way; rather than use the most common bit, the least common bit from each position is used.
    // Use the binary numbers in your diagnostic report to calculate the gamma rate and epsilon rate, then multiply them together.
    // What is the power consumption of the submarine?
    fun part1(input: List<String>): Int {
        val initValues = IntArray(input.first().length)
        fun evaluate(p: (Int) -> (Int)): Int {
            return input
                .map { it.trim() }
                .flatMap {
                    it.mapIndexed { i, c -> Instruction(c, i) }
                }
                .fold(initValues) { acc, i ->
                    acc[i.index] = acc[i.index] + if (i.c == '0') +1 else -1
                    acc
                }
                .map { p(it) }
                .fold("") {acc, i -> acc + i.toString() }
                .toInt(2)
        }

        val gamma = evaluate { if (it > 0) 0 else 1 }
        val epsilon = evaluate { if (it < 0) 0 else 1 }

        return gamma * epsilon
    }

    // Verify the life support rating, which can be determined by multiplying the oxygen generator rating by the CO2 scrubber rating.
    // - Keep only numbers selected by the bit criteria for the type of rating value.
    // - If you only have one number left, stop; this is the rating value for which you are searching.
    // - Otherwise, repeat the process, considering the next bit to the right.
    // oxygen generator rating determine the most common value (0 or 1) in the current bit position
    // CO2 scrubber rating determine the least common value (0 or 1) in the current bit position
    // What is the life support rating of the submarine?
    fun part2(input: List<String>): Int {
        val report = input.map { it.trim() }

        fun evaluate(col: List<String>, p: (Int, Double) -> Boolean): Int {
            var c = col
            var index = 0
            do {
                val char = if (p(c.map { it.elementAt(index) }.count { it == '1' }, c.size / 2.0)) 1 else 0
                c = c.filter { it.elementAt(index).digitToInt() == char }
                index += 1
            } while (c.size > 1)
            return c.first().toInt(2)
        }

        val oxy = evaluate(report) { v1, v2 -> v1 >= v2 }
        val co2 = evaluate(report) { v1, v2 -> v1 < v2 }

        return oxy * co2
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input)) // 2954600
    println(part2(input)) // 1662846
}
