package gradle.plugin.android

import gradle.plugin.configure.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("jinProject.android.hilt")
        }

        configureKotlinAndroid()
    }
}
