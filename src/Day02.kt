fun main() {
    data class PosNaive(val x: Int, val depth: Int) {
        fun forward(v: String) = PosNaive(x + v.toInt(), depth)
        fun up(v: String) = PosNaive(x, depth - v.toInt())
        fun down(v: String) = PosNaive(x, depth + v.toInt())
        fun toResult(): Int = x * depth
    }

    data class PosAdv(val x: Int, val depth: Int, val aim: Int) {
        fun forward(v: String) = PosAdv(x + v.toInt(), depth + aim * v.toInt(), aim)
        fun up(v: String) = PosAdv(x, depth, aim - v.toInt())
        fun down(v: String) = PosAdv(x, depth, aim + v.toInt())
        fun toResult(): Int = x * depth
    }

    val regex = """(forward|up|down) (\d+)""".toRegex()
    fun parse(str: String) = regex.find(str)?.destructured ?: error("$str cannot be parsed")

    // https://adventofcode.com/2021/day/2
    // It seems like the submarine can take a series of commands like forward 1, down 2, or up 3.
    // - forward X increases the horizontal position by X units.
    // - down X increases the depth by X units.
    // - up X decreases the depth by X units.
    // What do you get if you multiply your final horizontal position by your final depth?
    fun part1(input: List<String>): Int {
        return input
            .map(::parse)
            .fold(PosNaive(0, 0)) { pos, ac ->
                when (ac.component1()) {
                    "forward" -> pos.forward(ac.component2())
                    "up" -> pos.up(ac.component2())
                    "down" -> pos.down(ac.component2())
                    else -> error("Non existent command")
                }
            }
            .toResult()
    }

    // In addition to horizontal position and depth, you'll also need to track a third value, aim, which also starts at 0.
    // The commands also mean something entirely different than you first thought:
    // - down X increases your aim by X units.
    // - up X decreases your aim by X units.
    // - forward X does two things:
    //   - It increases your horizontal position by X units.
    //   - It increases your depth by your aim multiplied by X.
    // What do you get if you multiply your final horizontal position by your final depth?
    fun part2(input: List<String>): Int {
        return input
            .map(::parse)
            .fold(PosAdv(0, 0, 0)) { pos, ac ->
                when (ac.component1()) {
                    "forward" -> pos.forward(ac.component2())
                    "up" -> pos.up(ac.component2())
                    "down" -> pos.down(ac.component2())
                    else -> error("Non existent command")
                }
            }
            .toResult()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
