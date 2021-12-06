fun main() {
    fun parseInput(input: List<String>) = input.first()
        .split(',')
        .map { it.toLong() }
        // maps number of days for each fish to array of frequencies
        .fold(LongArray(9)) {
            arr, v -> arr[v.toInt()] = arr[v.toInt()] + 1
            arr
        }

    // https://adventofcode.com/2021/day/6
    // One lanternfish might have 2 days left until it creates another lanternfish, while another might have 4.
    // So, each fish as a single number that represents the number of days until it creates a new lanternfish.
    // Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other number decreases by 1 if it was present at the start of the day.
    // How many lanternfish would there be after 80 days?
    fun part1(input: List<String>): Int {
        // list/array evolution is valid solution until the size of it an issue
        var fishes = input.first().split(',').map { it.toInt() }.toTypedArray()
        for(day in 1..80) {
            var tmpArr = emptyArray<Int>()
            for (fIdx in fishes.indices) {
                if (fishes[fIdx] == 0) {
                    fishes[fIdx] = 6
                    tmpArr += 8
                } else {
                    fishes[fIdx] = fishes[fIdx] - 1
                }
            }
            fishes += tmpArr
        }
        return fishes.size
    }

    // How many lanternfish would there be after 256 days?
    fun part2(input: List<String>): Long {
        // instead of each fish frequencies of fishes' lifetime are evaluated
        val days = 256
        val fishes = parseInput(input)
        for (day in 0 until days) {
            val breedingCount = fishes[0]
            for (fIdx in 1 until fishes.size) {
                fishes[fIdx - 1] = fishes[fIdx]
            }
            fishes[8] = breedingCount
            fishes[6] = fishes[6] + breedingCount
        }
        return fishes.sum()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
