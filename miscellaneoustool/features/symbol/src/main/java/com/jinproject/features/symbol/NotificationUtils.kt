package com.jinproject.features.symbol

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import com.jinproject.features.symbol.guildmark.SymbolOverlayService

fun NotificationManager.createChannel(
    name: String,
    desc: String,
    importance: Int = NotificationManager.IMPORTANCE_HIGH
) {
    val channel = NotificationChannel(SymbolOverlayService.CHANNEL_NAME, name, importance).apply {
        description = desc
        enableVibration(true)
        setShowBadge(true)
        enableLights(true)
        lightColor = Color.BLUE
    }

    createNotificationChannel(channel)
}