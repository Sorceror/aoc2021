fun main() {
    val opening = listOf('(', '[', '{', '<').toCharArray()
    val closing = listOf(')', ']', '}', '>').toCharArray()

    fun parseLines(input: List<String>): List<Pair<Char?, List<Char>?>> {
        return input.map {
            val chars = it.toCharArray().toMutableList()
            val stack = mutableListOf<Char>()
            var incorrectChar = ' '
            do {
                val c = chars.removeFirst()
                if (closing.contains(c)) {
                    val opC = if (stack.isNotEmpty()) stack.removeLast() else '?'
                    if (c != opC) {
                        incorrectChar = c
                    }
                } else {
                    stack.add(closing[opening.indexOf(c)])
                }
            } while (chars.isNotEmpty() && incorrectChar == ' ')
            Pair(
                if (incorrectChar != ' ') incorrectChar else null,
                if (incorrectChar == ' ') stack.toList().reversed() else null
            )
        }
    }

    // https://adventofcode.com/2021/day/10
    // The navigation subsystem syntax is made of several lines containing chunks (puzzle input).
    // Every chunk must open and close with one of four legal pairs of matching characters.
    // Some lines are incomplete, but others are corrupted.
    // Stop at the first incorrect closing character on each corrupted line.
    // Syntax error score for a line, take the first illegal character on the line
    // ')' 3 pts, ']' 57 pts, '}' 1197 pts, '>' 25137 pts
    // What is the total syntax error score for those errors?
    fun part1(input: List<String>): Int {
        val closingValues = arrayOf(3, 57, 1197, 25137)
        return parseLines(input)
            .mapNotNull { it.first }
            .sumOf { closingValues[closing.indexOf(it)] }
    }

    // Discard the corrupted lines. The remaining lines are incomplete.
    // Figure out the sequence of closing characters that complete all open chunks in the line.
    // Start with a total score of 0. Then, for each character, multiply the total score by 5
    // and then increase the total score by the point value given for the character in the following table:
    // ')' 1 pts, ']' 2 pts, '}' 3 pts, '>' 4 pts
    // Find the completion string for each incomplete line, score the completion strings, and sort the scores.
    // What is the middle score?
    fun part2(input: List<String>): Long {
        val o = parseLines(input)
            .mapNotNull { it.second }
            .map { it.fold(0L) { acc, c -> acc * 5 + closing.indexOf(c) + 1 } }
            .sorted()
        return o[o.size / 2]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
