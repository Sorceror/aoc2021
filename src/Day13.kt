fun main() {
    data class Dot(val x: Int, val y: Int) {
        fun fold(fold: Pair<String, Int>): Dot {
            if (fold.first == "x" && x > fold.second) {
                return Dot(2 * fold.second - x, y)
            }
            if (fold.first == "y" && y > fold.second) {
                return Dot(x, 2 * fold.second - y)
            }
            return this
        }
    }

    fun printDots(dots: Set<Dot>) {
        for (y in 0..dots.maxOf { it.y }) {
            print("${y}\t")
            for (x in 0..dots.maxOf { it.x }) {
                print(if (dots.contains(Dot(x, y))) "%" else " ")
            }
            println()
        }
        println()
    }

    fun parseInput(input: List<String>): Pair<Set<Dot>, List<Pair<String, Int>>> {
        val dots = input.takeWhile { it.isNotBlank() }.map { it.split(',') }.map { Dot(it[0].toInt(), it[1].toInt()) }.toSet()
        val regex = """.*([xy])=(\d+)""".toRegex()
        val folds = input.filter { it.startsWith("fold") }.map { regex.find(it)?.destructured ?: error("Regex failed") }.map { Pair(it.component1(), it.component2().toInt()) }
        return Pair(dots, folds)
    }

    // https://adventofcode.com/2021/day/13
    // The transparent paper is marked with random dots and includes instructions on how to fold it up (your puzzle input).
    // The first section is a list of dots on the transparent paper.
    // Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants you to fold the paper up or left.
    // How many dots are visible after completing just the first fold instruction on your transparent paper?
    fun part1(input: List<String>): Int {
        val (dots, folds) = parseInput(input)
        return folds.
            take(1)
            .fold(dots) { currDots, pair ->
                currDots.map { it.fold(pair) }.toSet()
            }.count()
    }

    // Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital letters.
    // What code do you use to activate the infrared thermal imaging camera system?
    fun part2(input: List<String>): Set<Dot> {
        val (dots, folds) = parseInput(input)
        return folds.fold(dots) { currDots, pair ->
            currDots.map { it.fold(pair) }.toSet()
        }
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    printDots(part2(input))
}
