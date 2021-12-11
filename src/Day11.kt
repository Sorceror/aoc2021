fun main() {
    data class Octopus(val idx: Int, var energy: Int, var flashes: Int = 0) {
        fun willFlash() = energy > 9
        fun incEnergy() { energy += 1 }
        fun step() {
            if (energy > 9) {
                energy = 0
                flashes += 1
            }
        }
    }
    data class Grid(val input: List<String>) {
        val width = input.first().length
        val arr = input.joinToString(separator = "").mapIndexed { idx, c -> Octopus(idx, c.digitToInt()) }

        fun validIdx(idx: Int) = idx >= 0 && idx < arr.size
        fun sameRow(idx1: Int, idx2: Int) = idx1.div(width) == idx2.div(width)
        fun previousRow(idx1: Int, idx2: Int) = idx1.div(width) == idx2.div(width) - 1
        fun nextRow(idx1: Int, idx2: Int) = idx1.div(width) == idx2.div(width) + 1

        fun neighbors(idx: Int): List<Octopus> = listOfNotNull(
            if (validIdx(idx - width - 1) && previousRow(idx - width - 1, idx)) arr[idx - width - 1] else null, // up-left
            if (validIdx(idx - width))                                               arr[idx - width]     else null, // up
            if (validIdx(idx - width + 1) && previousRow(idx - width + 1, idx)) arr[idx - width + 1] else null, // up-right
            if (validIdx(idx + 1) && sameRow(idx + 1, idx))                     arr[idx + 1]         else null, // right
            if (validIdx(idx + width + 1) && nextRow(idx + width + 1, idx))     arr[idx + width + 1] else null, // down-right
            if (validIdx(idx + width))                                               arr[idx + width]     else null, // down
            if (validIdx(idx + width - 1) && nextRow(idx + width - 1, idx))     arr[idx + width - 1] else null, // down-left
            if (idx > 0 && sameRow(idx - 1, idx))                                   arr[idx - 1]         else null  // left
        )

        fun step() {
            // whole grid + 1
            arr.forEach { it.incEnergy() }
            // select only those whole will flash immediately
            val toFlash = arr.filter { it.willFlash() }.toMutableList()
            val flashed = mutableSetOf<Int>()
            while(toFlash.isNotEmpty()) {
                val o = toFlash.removeFirst()
                val ns = neighbors(o.idx)
                ns.forEach { it.incEnergy() }
                flashed.add(o.idx)
                // add neighbors octopuses that should flash in this step as well without those who already flashed
                // or already in list that should still be processed
                toFlash.addAll(ns.filter { it.willFlash() && !flashed.contains(it.idx) && !toFlash.contains(it) })
            }
            arr.forEach { it.step() }
        }

        fun countFlashes(steps: Int): Int {
            (0 until steps).forEach { _ -> step() }
            return arr.sumOf { it.flashes }
        }

        fun syncStep(): Int {
            var count = 0
            do {
                count += 1
                step()
            } while (!arr.all { it.energy == 0 })
            return count
        }
    }
    // https://adventofcode.com/2021/day/11
    // There are 100 octopuses arranged neatly in a 10 by 10 grid.
    // Each octopus slowly gains energy over time and flashes brightly for a moment when its energy is full.
    // Each octopus has an energy level - puzzle input.
    // Step
    // - the energy level of each octopus increases by 1
    // - any octopus with an energy level greater than 9 flashes (including octopuses that are diagonally adjacent)
    // - any octopus that flashed during this step has its energy level set to 0
    // How many total flashes are there after 100 steps?
    fun part1(input: List<String>): Int {
        return Grid(input).countFlashes(100)
    }

    // Octopuses synchronize when all flash simultaneously
    // What is the first step during which all octopuses flash?
    fun part2(input: List<String>): Int {
        return Grid(input).syncStep()
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
