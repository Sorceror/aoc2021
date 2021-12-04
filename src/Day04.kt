fun main() {
    data class Pos(val n: Int, var m: Boolean)
    data class Board(var pos: List<Pos>) {
        val columnIdx = (0..4).map { intArrayOf(it, it + 5, it + 10, it + 15, it + 20 ) }
        fun mark(n: Int) = pos.filter { !it.m }.forEach { it.m = it.n == n }
        fun winningRow() = pos.windowed(5, 5).map { l -> l.all { it.m } }.any { it }
        fun winningColumn() = columnIdx.map { it.map { idx -> pos[idx] } }.map { l -> l.all { it.m } }.any { it }
        fun isWinning() = winningRow() || winningColumn()
        fun unmarkedSum() = pos.filter { !it.m }.sumOf { it.n }
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<Board>> {
        val numbers = input.first().split(',').map { it.toInt() }
        val boards = input.asSequence()
            .drop(2)
            .filter { it.isNotEmpty() }
            .windowed(5, 5)
            .map { it.joinToString(separator = " ") }
            .map { it.trim().split("\\s+".toRegex()).map(String::toInt).map { v -> Pos(v, false) } }
            .map { Board(it) }
            .toList()
        return Pair(numbers, boards)
    }

    // https://adventofcode.com/2021/day/4
    // Bingo is played on a set of boards each consisting of a 5x5 grid of numbers.
    // Numbers are chosen at random, and the chosen number is marked on all boards on which it appears.
    // If all numbers in any row or any column of a board are marked, that board wins.
    // The score of the winning board is the sum of all unmarked numbers multiplied by the number that was just called.
    // What will the final score be if you choose board that win first?
    fun part1(input: List<String>): Int {
        val (numbers, boards) = parseInput(input)
        var boardScore = 0
        var index = 0;

        do {
            for (board in boards) {
                board.mark(numbers[index])
                if (board.isWinning()) {
                    boardScore = numbers[index] * board.unmarkedSum()
                }
            }
            index += 1
        } while (boardScore == 0)

        return boardScore
    }

    // Figure out which board will win last. Once it wins, what would its final score be?
    fun part2(input: List<String>): Int {
        var (numbers, boards) = parseInput(input)
        var boardScore = 0
        var index = 0;

        do {
            for (board in boards) {
                board.mark(numbers[index])
                if (board.isWinning()) {
                    boardScore = numbers[index] * board.unmarkedSum()
                    boards = boards.filter { it != board }
                }
            }
            index += 1
        } while (boards.isNotEmpty())

        return boardScore
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
