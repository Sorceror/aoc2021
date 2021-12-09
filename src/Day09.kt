fun main() {
    data class Floor(val heightMap: List<String>) {
        val width = heightMap.first().length
        val arr = heightMap.joinToString(separator = "").map { it.digitToInt() }.toIntArray()

        fun neighbors(idx: Int) =
            listOfNotNull(
                if (idx - width >= 0) Pair(arr[idx - width], idx - width) else null, // up
                if ((idx + 1).div(width) == idx.div(width)) Pair(arr[idx + 1], idx + 1) else null, // right
                if (idx + width < arr.size) Pair(arr[idx + width], idx + width) else null, // down
                if (idx > 0 && (idx - 1).div(width) == idx.div(width)) Pair(arr[idx - 1], idx - 1) else null // left
            )

        fun lowPoints(): List<Pair<Int, Int>> {
            return arr.mapIndexed{ idx, v -> Pair(v, idx) }.filter { (cur, idx) ->
                neighbors(idx).all { cur < it.first }
            }
        }

        fun basinSizes(): List<Int> {
            return lowPoints().map {
                val pts = mutableListOf(it)
                val basin = mutableSetOf<Int>()
                do {
                    val c = pts.removeFirst()
                    basin.add(c.second)
                    pts.addAll(neighbors(c.second).filter { n -> n.first != 9 && n.first > c.first })
                } while (pts.isNotEmpty())
                basin.size
            }
        }
    }

    // https://adventofcode.com/2021/day/9
    // Smoke flows to the lowest point of the area it's in.
    // Each input number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a location can be.
    // Low points - the locations that are lower than any of its adjacent locations (4 adjacency).
    // What is the sum of the risk levels of all low points on your heightmap?
    fun part1(input: List<String>): Int {
        return Floor(input).lowPoints().sumOf { it.first + 1 }
    }

    // A basin is all locations that eventually flow downward to a single low point.
    // Locations of height 9 do not count as being in any basin, and all other locations will always be part of exactly one basin.
    // What do you get if you multiply together the sizes of the three largest basins?
    fun part2(input: List<String>): Int {
        return Floor(input).basinSizes().sorted().takeLast(3).reduceRight{ v, acc -> acc * v }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
