package aoc.days

import util.FileLoader

val day8 = fun() {

    val lines = FileLoader.loadFileAsLines("/day8_input.txt")

    /*   0 1 2
     *   4 5 6
     *   7 8 9
     *
     *   array[row][column]
     *
     *   array[0][0] -> 0
     *   array[1][0] -> 4
     *   array[0][1] -> 1
     */
    val treeHeights = lines
        .map { line -> line.chunked(1).map { it.toInt() }.toIntArray() }
        .toTypedArray()

    val isVisibleFromTop = treeHeights.computeViewMap(Direction.FROM_TOP)
    val isVisibleFromLeft = treeHeights.computeViewMap(Direction.FROM_LEFT)
    val isVisibleFromRight = treeHeights.computeViewMap(Direction.FROM_RIGHT)
    val isVisibleFromBottom = treeHeights.computeViewMap(Direction.FROM_BOTTOM)

    val numberOfVisibleTrees = treeHeights.countCellsMatching { row, col ->
        isVisibleFromTop[row][col] ||
                isVisibleFromLeft[row][col] ||
                isVisibleFromRight[row][col] ||
                isVisibleFromBottom[row][col]
    }

    println(numberOfVisibleTrees)


    var topScenicValue = 0
    (treeHeights.indices).forEach { row ->
        (treeHeights[0].indices).forEach { col ->
            val sceneValue = treeHeights.computeScenicValueForCell(row, col)
            if (sceneValue > topScenicValue) topScenicValue = sceneValue
        }
    }
    println(topScenicValue)
}

private fun Array<IntArray>.countCellsMatching(predicate: (row: Int, col: Int) -> Boolean): Int {
    var totalMatching = 0
    (indices).forEach { rowIndex ->
        (this[0].indices).forEach { colIndex ->
            if (predicate(rowIndex, colIndex)) totalMatching += 1
        }
    }
    return totalMatching
}

private fun Array<IntArray>.computeViewMap(direction: Direction): Array<BooleanArray> {
    val viewArray = createVisibilityArray(this.size, this[0].size)

    when (direction) {
        Direction.FROM_TOP -> this.computeTopViewMap(viewArray)
        Direction.FROM_BOTTOM -> this.computeBottomViewMap(viewArray)
        Direction.FROM_LEFT -> this.computeLeftViewMap(viewArray)
        Direction.FROM_RIGHT -> this.computeRightViewMap(viewArray)
    }

    return viewArray
}

private fun createVisibilityArray(rows: Int, cols: Int) = BooleanArray(rows).map { BooleanArray(cols) }.toTypedArray()

private fun Array<IntArray>.computeTopViewMap(toViewArray: Array<BooleanArray>) {
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    (0 until numberOfColumns).forEach { processingColumnIndex ->
        this.runningChangeCellOnColumnInEveryRow(
            onColumnIndex = processingColumnIndex,
            rowIndexProgression = (0 until numberOfRows),
            operation = { prev, curr -> if (curr > prev) curr else prev },
            targetViewArray = toViewArray
        )
    }
}

private fun Array<IntArray>.computeBottomViewMap(toViewArray: Array<BooleanArray>) {
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    (0 until numberOfColumns).forEach { processingColumnIndex ->
        this.runningChangeCellOnColumnInEveryRow(
            onColumnIndex = processingColumnIndex,
            rowIndexProgression = (0 until numberOfRows).reversed(),
            operation = { prev, curr -> if (curr > prev) curr else prev },
            targetViewArray = toViewArray
        )
    }
}

private fun Array<IntArray>.computeLeftViewMap(toViewArray: Array<BooleanArray>) {
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    (0 until numberOfRows).forEach { processingRowIndex ->
        this.runningChangeCellOnRowInEveryColumn(
            onRowIndex = processingRowIndex,
            columnIndexProgression = (0 until numberOfColumns),
            operation = { prev, curr -> if (curr > prev) curr else prev },
            targetViewArray = toViewArray
        )
    }
}

private fun Array<IntArray>.computeRightViewMap(toViewArray: Array<BooleanArray>) {
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    (0 until numberOfRows).forEach { processingRowIndex ->
        this.runningChangeCellOnRowInEveryColumn(
            onRowIndex = processingRowIndex,
            columnIndexProgression = (0 until numberOfColumns).reversed(),
            operation = { prev, curr -> if (curr > prev) curr else prev },
            targetViewArray = toViewArray
        )
    }
}

private fun Array<IntArray>.runningChangeCellOnRowInEveryColumn(
    onRowIndex: Int,
    initialValue: Int = -1,
    columnIndexProgression: IntProgression,
    operation: (acc: Int, curr: Int) -> Int,
    targetViewArray: Array<BooleanArray>
): Array<BooleanArray> {
    var runningValue = initialValue
    columnIndexProgression.forEach { columnIndex ->
        targetViewArray[onRowIndex][columnIndex] = this[onRowIndex][columnIndex] > runningValue
        runningValue = operation(runningValue, this[onRowIndex][columnIndex])
    }
    return targetViewArray
}

private fun Array<IntArray>.runningChangeCellOnColumnInEveryRow(
    onColumnIndex: Int,
    initialValue: Int = -1,
    rowIndexProgression: IntProgression,
    operation: (prev: Int, curr: Int) -> Int,
    targetViewArray: Array<BooleanArray>
): Array<BooleanArray> {
    var runningValue = initialValue
    rowIndexProgression.forEach { rowIndex ->
        targetViewArray[rowIndex][onColumnIndex] = this[rowIndex][onColumnIndex] > runningValue
        runningValue = operation(runningValue, this[rowIndex][onColumnIndex])
    }
    return targetViewArray
}

enum class Direction {
    FROM_TOP,
    FROM_LEFT,
    FROM_RIGHT,
    FROM_BOTTOM;
}


// Task 2
private fun Array<IntArray>.computeScenicValueForCell(rowIndex: Int, colIndex: Int): Int {
    val numberOfColumns = this[0].size
    val numberOfRows = this.size
    val treeHeight = this[rowIndex][colIndex]

    val toLeft = colIndex - 1 downTo 0
    val toRight = colIndex + 1 until numberOfColumns
    val toTop = rowIndex - 1 downTo 0
    val toBottom = rowIndex + 1 until numberOfRows

    val left = toLeft.takeUntilLastIncluded { treeHeight <= this[rowIndex][it] }
    val right = toRight.takeUntilLastIncluded { treeHeight <= this[rowIndex][it] }

    val top = toTop.takeUntilLastIncluded { treeHeight <= this[it][colIndex] }
    val bot = toBottom.takeUntilLastIncluded { treeHeight <= this[it][colIndex] }

    return left.size * right.size * top.size * bot.size
}

@OptIn(ExperimentalStdlibApi::class)
private fun IntProgression.takeUntilLastIncluded(predicate: (Int) -> Boolean) = buildList<Int> {
    for (i in this) {
        add(i)
        if (predicate(i)) {
            return@buildList
        }
    }
}
