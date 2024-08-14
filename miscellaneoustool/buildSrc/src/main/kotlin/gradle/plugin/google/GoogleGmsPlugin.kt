package gradle.plugin.google

import gradle.plugin.extension.getVersionCatalog
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class GoogleGmsPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val libs = getVersionCatalog()

        with(pluginManager) {
            apply("com.google.gms.google-services")
        }

        dependencies {
            "implementation"(libs.findLibrary("google.gms.services.ads").get())
        }
    }
}