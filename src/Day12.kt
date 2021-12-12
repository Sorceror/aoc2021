fun main() {
    fun List<String>.parseInput() = this.map { it.split("-") }.map { s -> listOf(Pair(s[0], s[1]), Pair(s[1], s[0])) }.flatten().groupBy({it.first}, {it.second})
    fun String.isSmallCave() = this[0].isLowerCase()

    fun pathCountSmallOnlyOnce(cave: String, seen: Set<String>, count: Int, cons: Map<String, List<String>>): Int {
        // do we end recursion here?
        if (cave != "end") {
            // only small caves counts as visited, BIG can be visited any number of times
            val seenSoFar = if (cave.isSmallCave()) seen + cave else seen
            // filter neighbors to see by those we already saw (only small counts)
            val ns = cons[cave]?.filter { !seen.contains(it) } ?: emptyList()
            // recursion for all valid neighbors
            return count + ns.sumOf { pathCountSmallOnlyOnce(it, seenSoFar, count, cons) }
        }
        return count + 1
    }

    fun pathCountSingleSmallTwice(cave: String, seen: List<String>, count: Int, cons: Map<String, List<String>>): Int {
        if (cave != "end") {
            val seenSoFar = if (cave.isSmallCave()) seen + cave else seen
            val anyTwice = seenSoFar.groupingBy { it }.eachCount().any { it.value > 1 }
            // filter neighbors - can't return to start, either neighbor not seen yet or seen but there has been no other neighbor seen twice yet
            val ns = cons[cave]?.filter { it != "start" && (!seenSoFar.contains(it) || !anyTwice) } ?: emptyList()
            return count + ns.sumOf { pathCountSingleSmallTwice(it, seenSoFar, count, cons) }
        }
        return count + 1
    }

    // https://adventofcode.com/2021/day/12
    // Puzzle input is connection list.
    // How many paths through this cave system are there that visit small caves at most once?
    fun part1(input: List<String>): Int {
        return pathCountSmallOnlyOnce("start", emptySet(), 0, input.parseInput())
    }

    // How many paths through this cave system are there that visit single small caves at most twice (the rest only once)?
    fun part2(input: List<String>): Int {
        return pathCountSingleSmallTwice("start", emptyList(), 0, input.parseInput())
    }

    val testInputA = readInput("Day12_test_a")
    check(part1(testInputA) == 10)
    check(part2(testInputA) == 36)
    val testInputB = readInput("Day12_test_b")
    check(part1(testInputB) == 19)
    check(part2(testInputB) == 103)
    val testInputC = readInput("Day12_test_c")
    check(part1(testInputC) == 226)
    check(part2(testInputC) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
