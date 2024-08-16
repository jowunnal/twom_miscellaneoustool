package com.jinproject.features.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

fun checkAuthorityDrawOverlays(
    context: Context,
    registerForActivityResult: (Intent) -> Unit,
): Boolean { // 다른앱 위에 그리기 체크 : true = 권한있음 , false = 권한없음
    return if (!Settings.canDrawOverlays(context)) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        )
        registerForActivityResult(intent)
        false
    } else {
        true
    }
}

fun checkPermissions(
    context: Context,
    permissions: Set<String>,
    onGranted: () -> Unit,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {
    val granted = permissions.filter { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    val deniedPermissions = permissions.filter { permission ->
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_DENIED
    }

    if (granted.isNotEmpty())
        onGranted()
    else
        permissionLauncher.launch(deniedPermissions.toTypedArray())
}