package com.jinproject.features.symbol

import android.app.Notification
import android.app.Service
import android.os.Build

fun Service.startForegroundOnBuildVersion(channelId: Int, notification: Notification, serviceType: Int) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
        startForeground(channelId, notification, serviceType)
    else
        startForeground(channelId, notification)
