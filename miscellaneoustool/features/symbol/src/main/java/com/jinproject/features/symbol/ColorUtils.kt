package com.jinproject.features.symbol

import kotlin.math.sqrt

fun areColorsSimilar(color1: Int, color2: Int, threshold: Double): Boolean {
    val distance = calculateColorDistance(color1, color2)
    return distance <= threshold
}

internal fun calculateColorDistance(color1: Int, color2: Int): Double {
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