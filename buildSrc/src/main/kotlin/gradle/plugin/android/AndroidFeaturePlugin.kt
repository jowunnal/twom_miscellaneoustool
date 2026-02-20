package gradle.plugin.android

import gradle.plugin.configure.configureAndroidTest
import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidFeaturePlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("jinProject.android.library")
            apply("jinProject.android.hilt")
            apply("jinProject.android.compose")
        }

        configureAndroidTest()

        val libs = getVersionCatalog()
        dependencies {
            "implementation"(project(":core"))
            "implementation"(project(":domain"))
            "implementation"(project(":design"))
            "implementation"(project(":features:core"))

            "implementation"(libs.findBundle("navigation").get())
            "implementation"(libs.findBundle("lifecycle").get())
            "implementation"(libs.findLibrary("kotlinx-collections-immutable").get())
            "implementation"(libs.findLibrary("coil").get())
        }
    }
}
