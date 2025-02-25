package com.jinproject.features.symbol.guildmark

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.jinproject.features.symbol.areColorsSimilar
import kotlin.math.roundToInt

@Composable
fun rememberGuildMarkManager(
    bitMap: Bitmap,
    slider: Float,
): GuildMarkManager {

    val state = remember(bitMap) {
        GuildMarkManager().apply {
            setColors(bitMap)
        }
    }

    SideEffect {
        state.setSliderPos(slider)
    }

    return state
}

@Stable
class GuildMarkManager(
    private val cellCount: Int = 12,
) {
    var selectedColor by mutableStateOf(Color.Unspecified)
        private set

    var originColors = mutableStateListOf<Int>()

    val filteredCellColors
        get() = if (selectedColor == Color.Unspecified)
            originColors
        else
            originColors.map { color ->
                if (color == selectedColor.toArgb()) {
                    color
                } else
                    Color.White.toArgb()
            }

    var standardColors = mutableStateListOf<Int>()
        private set

    private var threshold by mutableDoubleStateOf(0.0)
    private val list by lazy { ArrayList<IntArray>(cellCount) }

    fun selectColor(color: Color) {
        selectedColor = color
    }

    fun setSliderPos(pos: Float) {
        threshold = pos.toDouble()
        setPlatte()
    }

    fun setColors(bitMap: Bitmap) {
        var width = 0
        var height = 0
        if (bitMap.width > bitMap.height) {
            width = cellCount
            height = (bitMap.height * (width / bitMap.width.toFloat())).roundToInt()
        } else {
            height = cellCount
            width = (bitMap.width * (height / bitMap.height.toFloat())).roundToInt()
        }

        val colors = IntArray(width * height)
        val resizedBitMap = Bitmap.createScaledBitmap(bitMap, width, height, true)
        resizedBitMap.getPixels(colors, 0, width, 0, 0, width, height)

        val intArray = IntArray(cellCount) { Color.White.toArgb() }
        var row = 0

        list.clear()

        if (width == cellCount) {
            val diff = width - height
            repeat(diff / 2) {
                list.add(IntArray(cellCount))
            }
            colors.forEachIndexed { index, color ->
                if (index % width == 0 && index != 0) {
                    list.add(intArray.clone())
                    row = 0
                }
                intArray[row++] = color
            }
            repeat(diff / 2 + diff % 2) {
                list.add(IntArray(cellCount))
            }
        } else {
            val diff = height - width
            row = diff / 2
            colors.forEachIndexed { index, color ->
                if (index % width == 0 && index != 0) {
                    list.add(intArray.clone())
                    row = diff / 2
                }

                intArray[row++] = color
            }
        }

        setPlatte()
    }

    private fun setPlatte() {
        val distinctColors = list.distinctColors().sorted()
        standardColors.clear()
        standardColors.addAll(distinctColors.distinct())

        val filteredColors = list.flatFilteredColorList(distinctColors)

        originColors.clear()
        originColors.addAll(filteredColors)
    }

    private fun List<IntArray>.distinctColors(): List<Int> {
        val colorList = arrayListOf<Int>()
        for (colors in this) {
            for (color in colors) {
                if (colorList.isNotEmpty()) {
                    var isSimilar = false
                    run loop@{
                        colorList.forEach { c ->
                            if (areColorsSimilar(c, color, threshold)) {
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

    private fun ArrayList<IntArray>.flatFilteredColorList(distinctColors: List<Int>) =
        this.flatMap { row ->
            val intArray = IntArray(row.size)

            row.forEachIndexed { index, cell ->
                for (c in distinctColors) {
                    if (areColorsSimilar(c, cell, threshold)) {
                        intArray[index] = c
                        break
                    }
                }
            }

            intArray.toList()
        }

    companion object {
        fun getInitBitmap() = Bitmap.createBitmap(12, 12, Bitmap.Config.ARGB_8888)
    }
}