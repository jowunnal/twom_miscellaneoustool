package gradle.plugin.android

import gradle.plugin.configure.configureTest
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

        configureTest()

        val libs = getVersionCatalog()
        dependencies {
            "implementation"(project(":core"))
            "implementation"(project(":domain"))
            "implementation"(project(":design-compose"))
            "implementation"(project(":design-ui"))
            "implementation"(project(":features:core"))

            "implementation"(libs.findLibrary("hilt.navigation.compose").get())
            "implementation"(libs.findBundle("lifecycle").get())
            "implementation"(libs.findLibrary("kotlinx-collections-immutable").get())
            "implementation"(libs.findBundle("navigation").get())
            "implementation"(libs.findLibrary("bumptech.glide").get())
            "implementation"(libs.findLibrary("coil").get())
        }
    }
}
