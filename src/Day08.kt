fun main() {
    // https://adventofcode.com/2021/day/8
    // In the output values, how many times do digits 1, 4, 7, or 8 appear?
    fun part1(input: List<String>): Int {
        return input
            .map { it.split('|') }
            .map { it[1].split(' ') }
            .flatMap { it.map { str -> str.length } }
            .count { it == 2 || it == 4 || it == 3 || it == 7 }
    }

    // For each entry, determine all the wire/segment connections and decode the four-digit output values.
    // What do you get if you add up all the output values?
    fun part2(input: List<String>): Int {
        val codes = input
            .map { it.split('|') }
            .map { it.map { str -> str.trim().split(' ').map { code -> code.toCharArray().toSet() } } }
            .map { Pair(it[0], it[1]) }
        val digitValues = mapOf(
            setOf('a',      'c', 'd', 'e',      'g') to '2', // 5 / bf
            setOf('a',      'c', 'd',      'f', 'g') to '3', // 5 / be
            setOf('a', 'b',      'd',      'f', 'g') to '5', // 5 / ce
            setOf('a', 'b', 'c',      'e', 'f', 'g') to '0', // 6 * d
            setOf('a', 'b',      'd', 'e', 'f', 'g') to '6', // 6 * c
            setOf('a', 'b', 'c', 'd',      'f', 'g') to '9', // 6 * e
            setOf(          'c',           'f'     ) to '1', // 2 - abdeg
            setOf('a',      'c',           'f'     ) to '7', // 3 - bdeg
            setOf(     'b', 'c', 'd',      'f'     ) to '4', // 4 - aeg
            setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to '8'  // 7 -
        )
        // 3 - 2                        -> a
        // 5 (miss bcef) - 4 (miss aeg) -> e
        // 5 (without e) v 5 (with e)   -> b
        // 5 (with b) - 5 (without e)   -> f
        // 1 - f                        -> c
        // 4 - bcf                      -> d
        // 7 - abcdef                   -> g
        return codes.sumOf {
            val digits = it.first
            val all = digits.first { code -> code.size == 7 }
            val two = digits.first { code -> code.size == 2 }
            val three = digits.first { code -> code.size == 3 }
            val fours = digits.filter { code -> code.size == 4 }
            val foursInv = fours.map { s -> all.subtract(s.toSet()) }
            val fives = digits.filter { code -> code.size == 5 }
            val fivesInv = fives.map { s -> all.subtract(s.toSet()) }

            // (a, c, f) - (c, f) -> a
            val a = three.subtract(two).first()
            // missing in 5s ((b,f) ∪ (b, e) ∪ (c, e)) ∩ missing in 4 (a, e, g) -> e
            val e = fivesInv.flatten().toSet().intersect(foursInv.flatten().toSet()).first()
            // 5s with e ((b, e) ∪ (c, e)) ∪ 5s without e (b, f) -> b
            val b = fives.filter { set -> !set.contains(e) }.map { set -> all.subtract(set) }.flatten().toSet().intersect(all.subtract(fives.filter { set -> set.contains(e) }.flatten().toSet())).first()
            // missing in 5s with b ((b, f) ∪ (b, e)) - (b, e) -> f
            val f = fivesInv.filter { c -> c.contains(b) }.flatten().toSet().subtract(setOf(b, e)).first()
            // (c, f) - (f) -> c
            val c = two.subtract(setOf(f)).first()
            // (b, c, d, f) - (b, c, f) -> d
            val d = fours.flatten().toSet().subtract(setOf(b, c, f)).first()
            // (a, b, c, d, e, f, g) - (a, b, c, d, e, f) -> g
            val g = all.subtract(setOf(a, b, c, d, e, f)).first()
            val mappedCodes = mapOf(a to 'a', b to 'b', c to 'c', d to 'd', e to 'e', f to 'f', g to 'g')
            val value = it.second
            value.map { di -> di.map { character -> mappedCodes[character]!! }.toSet() }.map { di -> digitValues[di]!! }.joinToString(separator = "").toInt()
        }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
