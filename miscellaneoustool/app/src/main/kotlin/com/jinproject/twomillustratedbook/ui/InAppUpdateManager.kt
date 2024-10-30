package com.jinproject.twomillustratedbook.ui

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

internal class InAppUpdateManager(
    private val activity: Activity,
    private val requestInAppUpdatingLauncher: ((ActivityResult) -> Unit) -> ActivityResultLauncher<IntentSenderRequest>,
    private val showDialog: (AppUpdateManager, () -> Unit) -> Unit,
) {
    private val appUpdateManager = AppUpdateManagerFactory.create(activity)

    private var installUpdatedListener: InstallStateUpdatedListener? = null

    fun checkUpdateIsAvailableOrDownloaded() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                requestInAppUpdate(appUpdateInfo)
            }
            else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                requireInstalling()
            }
        }
    }

    private fun requestInAppUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            requestInAppUpdatingLauncher(::inAppUpdatingLauncherResult),
            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
        )
    }

    private fun inAppUpdatingLauncherResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            installUpdatedListener = InstallStateUpdatedListener { state ->
                if (state.installStatus() == InstallStatus.DOWNLOADED)
                    requireInstalling()
            }

            installUpdatedListener?.let { listener ->
                appUpdateManager.registerListener(listener)
                Snackbar.make(
                    activity.window.decorView.rootView,
                    activity.getString(com.jinproject.design_ui.R.string.updating_new_version),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            sendMessageIfDenyUpdate()
        }
    }

    private fun requireInstalling() {
        installUpdatedListener?.let { listener ->
            appUpdateManager.unregisterListener(listener)
            installUpdatedListener = null
        }

        showDialog(appUpdateManager, ::sendMessageIfDenyUpdate)
    }

    private fun sendMessageIfDenyUpdate() {
        Snackbar.make(
            activity.window.decorView.rootView,
            activity.getString(com.jinproject.design_ui.R.string.deny_new_version_update),
            Snackbar.LENGTH_LONG,
        ).show()
    }
}