package com.jinproject.features.symbol.guildmark

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun rememberGuildMarkManager(
    bitMap: Bitmap,
): GuildMarkManager {

    val state = remember {
        GuildMarkManager().apply {
            setColors(bitMap)
        }
    }

    return state
}

@Stable
class GuildMarkManager(
    private val cellCount: Int = 12,
) {
    var selectedColor by mutableStateOf(Color.Unspecified)
        private set

    var originColors = mutableStateListOf(IntArray(cellCount))

    val filteredCellColors
        get() = if (selectedColor == Color.Unspecified)
            originColors
        else
            originColors.map { row ->
                row.map { color ->
                    if (color == selectedColor.toArgb()) {
                        color
                    } else
                        Color.White.toArgb()
                }.toIntArray()
            }

    var standardColors = mutableStateListOf<Int>()
        private set

    fun List<IntArray>.distinctColors(): List<Int> {
        val colorList = arrayListOf<Int>()
        for (colors in this) {
            for (color in colors) {
                if (colorList.isNotEmpty()) {
                    var isSimilar = false
                    run loop@{
                        colorList.forEach { c ->
                            if (areColorsSimilar(c, color)) {
                                isSimilar = true
                                return@loop
                            }
                        }
                    }

                    if (!isSimilar)
                        colorList.add(color)
                } else
                    colorList.add(color)
            }
        }

        return colorList
    }

    fun selectColor(color: Color) {
        selectedColor = color
    }

    fun setColors(bitMap: Bitmap) {
        var width = 0
        var height = 0
        if(bitMap.width > bitMap.height) {
            width = cellCount
            height = (bitMap.height * (width / bitMap.width.toFloat())).roundToInt()
        } else {
            height = cellCount
            width = (bitMap.width * (height / bitMap.height.toFloat())).roundToInt()
        }
        val resizedBitMap = Bitmap.createScaledBitmap(bitMap, cellCount, cellCount, true)

        val list = ArrayList<IntArray>()
        repeat(cellCount) { row ->
            val intArray = IntArray(cellCount)
            repeat(cellCount) { col ->
                intArray[col] = kotlin.runCatching {
                    resizedBitMap.getPixel(col, row)
                }.getOrElse {
                    Color.White.toArgb()
                }
            }
            list.add(intArray)
        }
        //Log.d("test","aa")

        standardColors.clear()
        standardColors.addAll(list.distinctColors().sorted())

        val filteredColors = list.map { row ->
            val intArray = IntArray(row.size)

            row.forEachIndexed { index, cell ->
                for (c in standardColors) {
                    if (areColorsSimilar(c, cell)) {
                        intArray[index] = c
                        break
                    }
                }
            }

            intArray
        }
        originColors.clear()
        originColors.addAll(filteredColors)
    }

    private fun calculateColorDistance(color1: Int, color2: Int): Double {
        val red1 = android.graphics.Color.red(color1)
        val green1 = android.graphics.Color.green(color1)
        val blue1 = android.graphics.Color.blue(color1)

        val red2 = android.graphics.Color.red(color2)
        val green2 = android.graphics.Color.green(color2)
        val blue2 = android.graphics.Color.blue(color2)

        val deltaRed = (red1 - red2).toDouble()
        val deltaGreen = (green1 - green2).toDouble()
        val deltaBlue = (blue1 - blue2).toDouble()

        return sqrt(deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue)
    }

    fun areColorsSimilar(color1: Int, color2: Int, threshold: Double = 0.0): Boolean {
        val distance = calculateColorDistance(color1, color2)
        return distance <= threshold
    }
}