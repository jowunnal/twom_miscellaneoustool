package gradle.plugin.android

import gradle.plugin.configure.configureKotlinAndroid
import gradle.plugin.extension.androidExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("jinProject.android.hilt")
            apply("jinProject.android.compose")
            apply("jinProject.android.gms-services")
            apply("jinProject.android.firebase")
        }

        androidExtension.apply {
            defaultConfig {
                vectorDrawables {
                    useSupportLibrary = true
                }
            }
        }

        configureKotlinAndroid()
    }
}
