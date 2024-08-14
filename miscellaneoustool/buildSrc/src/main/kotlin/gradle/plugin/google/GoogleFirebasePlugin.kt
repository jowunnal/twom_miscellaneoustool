package gradle.plugin.google

import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class GoogleFirebasePlugin: Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        val libs = getVersionCatalog()

        with(pluginManager) {
            apply("com.google.firebase.crashlytics")
        }

        dependencies {
            "implementation"(platform(libs.findLibrary("firebase.bom").get()))
            "implementation"(libs.findBundle("firebase").get())
        }
    }
}