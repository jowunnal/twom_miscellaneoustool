package gradle.plugin.android

import gradle.plugin.configure.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class AndroidLibraryPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        configureKotlinAndroid()
    }
}
