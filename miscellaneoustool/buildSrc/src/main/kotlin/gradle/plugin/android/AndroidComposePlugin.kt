package gradle.plugin.android

import gradle.plugin.extension.androidExtension
import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = getVersionCatalog()

        androidExtension.apply {
            buildFeatures {
                compose = true
            }
        }

        with(pluginManager) {
            apply("org.jetbrains.kotlin.plugin.compose")
            apply("kotlinx-serialization")
            apply("kotlin-parcelize")
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))

            "implementation"(libs.findBundle("compose").get())
            "debugImplementation"(libs.findLibrary("compose.ui.tooling").get())

            "implementation"(libs.findLibrary("navigation.compose").get())
            "implementation"(libs.findBundle("composeAdaptive").get())
            "implementation"(libs.findLibrary("kotlinx-serialization-json").get())
        }
    }
}
