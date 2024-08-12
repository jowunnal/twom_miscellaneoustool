package gradle.plugin.android

import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidGmsServicePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = getVersionCatalog()

        with(pluginManager) {
            apply("com.google.gms.google-services")
            apply("com.google.firebase.crashlytics")
        }

        dependencies {
            "implementation"(platform(libs.findLibrary("firebase.bom").get()))
            "implementation"(libs.findBundle("firebase").get())
        }
    }
}